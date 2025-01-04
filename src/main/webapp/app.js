const API_URL = "http://localhost:8081/api/todos";

document.addEventListener("DOMContentLoaded", () => {
    const deadlineInput = document.getElementById("deadline");
    const today = new Date().toISOString().split("T")[0]; // Get today's date in YYYY-MM-DD format
    deadlineInput.setAttribute("min", today); // Set the min attribute to today's date
});

async function fetchTasks() {
    const response = await fetch(API_URL);
    const tasks = await response.json();

    const taskList = document.getElementById("task-list");
    taskList.innerHTML = "";

    tasks.forEach(task => {
        const taskDiv = document.createElement("div");
        taskDiv.className = `task ${task.isCompleted ? "completed" : ""}`;
        taskDiv.innerHTML = `
            <strong>${task.title}</strong><br>
            Deadline: ${task.deadline}<br>
            Completed: ${task.isCompleted ? "Yes" : "No"}<br>
            <button onclick="deleteTask(${task.id})">Delete</button>
            <button onclick="markAsCompleted(${task.id})">Mark as Completed</button>
        `;
        taskList.appendChild(taskDiv);
    });
}

async function addTask(event) {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const deadline = document.getElementById("deadline").value;

    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            title: title,
            deadline: deadline,
            isCompleted: false,
        }),
    });

    if (response.ok) {
        alert("Task added successfully!");
        fetchTasks();
    } else {
        alert("Failed to add task.");
    }
}

async function deleteTask(id) {
    console.log(API_URL);
    const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
    });

    if (response.ok) {
        alert("Task deleted successfully!");
        fetchTasks();
    } else {
        alert("Failed to delete task.");
    }
}

async function markAsCompleted(id) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            isCompleted: true,
        }),
    });

    if (response.ok) {
        alert("Task marked as completed!");
        fetchTasks();
    } else {
        alert("Failed to update task.");
    }
}

document.getElementById("task-form").addEventListener("submit", addTask);
fetchTasks();
