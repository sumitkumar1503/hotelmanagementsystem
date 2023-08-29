// This is for able to see chart. We are using Apex Chart. U can check the documentation of Apex Charts too..

var sidebarOpen = false;
var sidebar = document.getElementById("sidebar");
var sidebarCloseIcon = document.getElementById("sidebarIcon");

function toggleSidebar() {
    if (!sidebarOpen) {
        sidebar.classList.add("sidebar_responsive");
        sidebarOpen = true;
    }
}

function closeSidebar() {
    if (sidebarOpen) {
        sidebar.classList.remove("sidebar_responsive");
        sidebarOpen = false;
    }
}