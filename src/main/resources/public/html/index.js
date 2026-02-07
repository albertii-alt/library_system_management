const API_URL = "http://localhost:8080";

// --- Show Form ---
// --- Show Form ---
function showForm(id) {
    const wrapper = document.querySelector(".form-section-wrapper");
    const section = document.getElementById(id);

    // Hide all forms first
    document.querySelectorAll(".form-section").forEach(s => {
        s.classList.remove("show");
        s.style.opacity = 0;
        s.style.display = "none";
    });

    // Temporarily display the selected form for measurement
    section.style.display = "block";
    section.style.opacity = 0;

    // Apply expanded class immediately
    wrapper.classList.add("expanded");

    // Set wrapper width to auto to measure full width
    wrapper.style.width = 'auto';
    const contentWidth = wrapper.scrollWidth + 20; // add optional padding
    wrapper.style.width = '0'; // reset to trigger transition

    // Trigger reflow
    wrapper.offsetHeight;

    // Animate to measured width
    wrapper.style.transition = "width 0.5s ease";
    wrapper.style.width = contentWidth + "px";

    // Fade in the selected form after expansion
    setTimeout(() => {
        section.classList.add("show");
        section.style.opacity = 1;
    }, 100);

    // Load dropdowns if needed
    if (id === "borrowBook") {
        loadMembers("borrowMember");
        loadBooks("borrowTitle", true);
    }
    if (id === "returnBook") {
        loadMembers("returnMember");
        loadBooks("returnTitle", false);
    }
}

// --- Show List ---
function showList(type) {
    const wrapper = document.querySelector(".form-section-wrapper");

    // Hide all forms/sections first
    document.querySelectorAll(".form-section").forEach(s => {
        s.classList.remove("show");
        s.style.display = "none";
        s.style.opacity = 0;
    });

    wrapper.classList.add("expanded");

    let section, listElement, url;

    if (type === "books") {
        section = document.getElementById("booksOutput");   // dedicated books section
        listElement = document.getElementById("booksList");
        url = `${API_URL}/books`;
    } else if (type === "members") {
        section = document.getElementById("membersOutput"); // dedicated members section
        listElement = document.getElementById("membersList");
        url = `${API_URL}/members`;
    } else {
        // Fallback for forms like borrowBook / returnBook
        section = document.getElementById(type);
        section.style.display = "block";
        section.style.opacity = 0;

        // Expand width animation
        wrapper.style.width = "auto";
        const contentWidth = section.scrollWidth + 20;
        wrapper.style.width = "0";
        wrapper.offsetHeight;
        wrapper.style.transition = "width 0.5s ease";
        wrapper.style.width = contentWidth + "px";

        // Fade in
        setTimeout(() => {
            section.classList.add("show");
            section.style.opacity = 1;
        }, 100);

        // Load dropdowns if needed
        if (type === "borrowBook") {
            loadMembers("borrowMember");
            loadBooks("borrowTitle", true);
        }
        if (type === "returnBook") {
            loadMembers("returnMember");
            loadBooks("returnTitle", false);
        }
        return; // stop here so it doesn't run the list-fetch part
    }

    // === Handle Books/Members list ===
    if (!section) return;

    section.style.display = "block";
    section.style.opacity = 0;
    listElement.innerHTML = "";

    fetch(url).then(res => res.json()).then(data => {
        if (data.length === 0) {
            listElement.innerHTML = `<li>No ${type} available</li>`;
        } else {
            if (type === "books") {
                data.forEach(b => {
                    const status = b.available ? "✅ Available" : "❌ Not Available";
                    listElement.innerHTML += `<li><strong>${b.title}</strong> by ${b.author} — ${status}</li>`;
                });
            } else {
                data.forEach(m => {
                    const borrowed = m.borrowedBooks && m.borrowedBooks.length > 0
                        ? `<ul>${m.borrowedBooks.map(b => `<li>${b.title} by ${b.author}</li>`).join("")}</ul>`
                        : "<em>No books borrowed</em>";
                    listElement.innerHTML += `<li><strong>${m.name}</strong> (${m.type})<br/>Borrowed: ${borrowed}</li>`;
                });
            }
        }

        // Expand width animation
        wrapper.style.width = "auto";
        const contentWidth = section.scrollWidth + 20;
        wrapper.style.width = "0";
        wrapper.offsetHeight;
        wrapper.style.transition = "width 0.5s ease";
        wrapper.style.width = contentWidth + "px";

        // Fade in
        setTimeout(() => {
            section.classList.add("show");
            section.style.opacity = 1;
        }, 100);
    });
}

// --- Load Members ---
function loadMembers(selectId) {
  fetch(`${API_URL}/members`).then(res => res.json()).then(data => {
    const select = document.getElementById(selectId);
    select.innerHTML = "";
    if (data.length === 0) select.innerHTML = `<option value="">No members</option>`;
    else data.forEach(m => {
      select.innerHTML += `<option value="${m.name}">${m.name} (${m.type})</option>`;
    });
  });
}
//Collapse Button
function collapseForm() {
  const wrapper = document.querySelector(".form-section-wrapper");

  // Hide all forms
  document.querySelectorAll(".form-section").forEach(s => {
    s.classList.remove("show");
    s.style.display = "none";
    s.style.opacity = 0;
  });

  // Collapse the panel
  wrapper.style.transition = "width 0.5s ease";
  wrapper.style.width = "0";
  wrapper.classList.remove("expanded");
}

// --- Load Books ---
function loadBooks(selectId, onlyAvailable) {
  fetch(`${API_URL}/books`).then(res => res.json()).then(data => {
    const select = document.getElementById(selectId);
    select.innerHTML = "";
    let filtered = onlyAvailable ? data.filter(b => b.available) : data.filter(b => !b.available);
    if (filtered.length === 0) select.innerHTML = `<option value="">No books</option>`;
    else filtered.forEach(b => {
      select.innerHTML += `<option value="${b.title}">${b.title} by ${b.author}</option>`;
    });
  });
}

// --- Add Book ---
function addBook() {
  const title = document.getElementById("bookTitle").value;
  const author = document.getElementById("bookAuthor").value;
  if (!title || !author) return alert("Please provide book title and author!");
  fetch(`${API_URL}/books`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ title, author })
  }).then(res => res.json()).then(data => {
    if (data.error) alert("❌ " + data.error);
    else {
      alert(`✅ Book added: ${data.title} by ${data.author}`);
      loadBooks("borrowTitle", true);
      loadBooks("returnTitle", false);
    }
    document.getElementById("bookTitle").value = "";
    document.getElementById("bookAuthor").value = "";
  });
}

// --- Add Member ---
function addMember() {
  const name = document.getElementById("memberName").value;
  const type = document.getElementById("memberType").value;
  if (!name) return alert("Please provide member name!");
  fetch(`${API_URL}/members`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, type })
  }).then(res => res.json()).then(data => {
    if (data.error) alert("❌ " + data.error);
    else {
      alert(`✅ Member registered: ${data.name} (${data.type})`);
      loadMembers("borrowMember");
      loadMembers("returnMember");
    }
    document.getElementById("memberName").value = "";
  });
}

// --- Borrow Book ---
function borrowBook() {
  const memberName = document.getElementById("borrowMember").value;
  const bookTitle = document.getElementById("borrowTitle").value;
  if (!memberName || !bookTitle) return alert("Please provide both member name and book title!");
  fetch(`${API_URL}/borrow`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ memberName, bookTitle })
  }).then(res => res.json()).then(data => {
    if (data.error) alert("❌ " + data.error);
    else {
      alert("✅ Book borrowed!");
      loadBorrowedBooks();
    }
  });
}

// --- Return Book ---
function returnBook() {
  const memberName = document.getElementById("returnMember").value;
  const bookTitle = document.getElementById("returnTitle").value;
  if (!memberName || !bookTitle) return alert("Please select a member and a book!");
  fetch(`${API_URL}/return`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ memberName, bookTitle })
  }).then(res => res.json()).then(data => {
    if (data.error) alert("❌ " + data.error);
    else alert("✅ " + data.message);
  });
}

// --- Load Borrowed Books ---
function loadBorrowedBooks() {
  const memberName = document.getElementById("returnMember").value;
  const bookDropdown = document.getElementById("returnTitle");
  if (!memberName) return;

  fetch(`${API_URL}/members`).then(res => res.json()).then(members => {
    const member = members.find(m => m.name === memberName);
    bookDropdown.innerHTML = "";
    if (member && member.borrowedBooks && member.borrowedBooks.length > 0) {
      member.borrowedBooks.forEach(b => {
        const option = document.createElement("option");
        option.value = b.title;
        option.textContent = `${b.title} by ${b.author}`;
        bookDropdown.appendChild(option);
      });
    } else {
      const option = document.createElement("option");
      option.textContent = "No borrowed books";
      option.disabled = true;
      option.selected = true;
      bookDropdown.appendChild(option);
    }
  });
}

// --- Exit System ---
function exitSystem() {
  if (confirm("Are you sure you want to exit?")) {
    window.location.replace("about:blank");
  }
}

/* 🌙 Dark Mode Toggle */
// --- Dark Mode Toggle ---
const toggle = document.getElementById("darkModeToggle");
toggle.addEventListener("change", () => {
  document.body.classList.toggle("dark-mode", toggle.checked);
});