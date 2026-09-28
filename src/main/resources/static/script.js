const API = "/api";


// ===============================
// SECTION NAVIGATION
// ===============================

function showSection(sectionId) {

    document.querySelectorAll(".section").forEach(section => {
        section.classList.remove("active");
    });

    document.getElementById(sectionId).classList.add("active");

    if (sectionId === "rooms") {
        loadRooms();
    }

    if (sectionId === "guests") {
        loadGuests();
    }

    if (sectionId === "bookings") {
        loadBookings();
    }
}


// ===============================
// MESSAGE
// ===============================

function showMessage(message, type = "success") {

    const box = document.getElementById("messageBox");

    box.innerHTML = `
        <div class="message ${type}">
            ${message}
        </div>
    `;

    setTimeout(() => {
        box.innerHTML = "";
    }, 3500);
}


// ===============================
// ROOMS
// ===============================

async function loadRooms() {

    try {

        const response = await fetch(`${API}/rooms`);

        if (!response.ok) {
            throw new Error("Unable to load rooms");
        }

        const rooms = await response.json();

        const table = document.getElementById("roomTableBody");

        table.innerHTML = "";

        rooms.forEach(room => {

            table.innerHTML += `
                <tr>
                    <td>${room.id}</td>
                    <td>${room.roomType}</td>
                    <td>${room.capacity}</td>
                    <td>₹${room.ratePerNight}</td>

                    <td>

                        <button
                            class="action-btn edit-btn"
                            onclick='editRoom(${JSON.stringify(room)})'>
                            Edit
                        </button>

                        <button
                            class="action-btn delete-btn"
                            onclick="deleteRoom(${room.id})">
                            Delete
                        </button>

                    </td>
                </tr>
            `;
        });

    } catch (error) {

        showMessage(error.message, "error");
    }
}
async function searchRooms() {

    const roomType =
        document.getElementById("roomSearchType").value.trim();

    if (!roomType) {

        showMessage(
            "Please enter a room type",
            "error"
        );

        return;
    }

    try {

        const response =
            await fetch(
                `${API}/rooms/search?roomType=${encodeURIComponent(roomType)}`
            );

        const rooms = await response.json();

        if (!response.ok) {

            throw new Error(
                rooms.message || "Unable to search rooms"
            );
        }

        const table =
            document.getElementById("roomTableBody");

        table.innerHTML = "";

        if (rooms.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="5">
                        No rooms found.
                    </td>
                </tr>
            `;

            return;
        }

        rooms.forEach(room => {

            table.innerHTML += `
                <tr>

                    <td>${room.id}</td>

                    <td>${room.roomType}</td>

                    <td>${room.capacity}</td>

                    <td>₹${room.ratePerNight}</td>

                    <td>

                        <button
                            class="action-btn edit-btn"
                            onclick='editRoom(${JSON.stringify(room)})'>
                            Edit
                        </button>

                        <button
                            class="action-btn delete-btn"
                            onclick="deleteRoom(${room.id})">
                            Delete
                        </button>

                    </td>

                </tr>
            `;
        });

    } catch (error) {

        showMessage(error.message, "error");
    }
}


async function saveRoom() {

    const id = document.getElementById("roomId").value;

    const room = {

        roomType:
        document.getElementById("roomType").value,

        capacity:
            Number(document.getElementById("roomCapacity").value),

        ratePerNight:
            Number(document.getElementById("roomRate").value)
    };

    try {

        let response;

        if (id) {

            response = await fetch(`${API}/rooms/${id}`, {

                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(room)
            });

        } else {

            response = await fetch(`${API}/rooms`, {

                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(room)
            });
        }

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Room operation failed"
            );
        }

        showMessage(
            id
                ? "Room updated successfully"
                : "Room added successfully"
        );

        clearRoomForm();

        loadRooms();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


function editRoom(room) {

    document.getElementById("roomId").value =
        room.id;

    document.getElementById("roomType").value =
        room.roomType;

    document.getElementById("roomCapacity").value =
        room.capacity;

    document.getElementById("roomRate").value =
        room.ratePerNight;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}


async function deleteRoom(id) {

    if (!confirm("Are you sure you want to delete this room?")) {
        return;
    }

    try {

        const response =
            await fetch(`${API}/rooms/${id}`, {
                method: "DELETE"
            });

        const message = await response.text();

        if (!response.ok) {
            throw new Error(message);
        }

        showMessage(message);

        loadRooms();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


function clearRoomForm() {

    document.getElementById("roomId").value = "";
    document.getElementById("roomType").value = "";
    document.getElementById("roomCapacity").value = "";
    document.getElementById("roomRate").value = "";
}


// ===============================
// GUESTS
// ===============================

async function loadGuests() {

    try {

        const response =
            await fetch(`${API}/guests`);

        if (!response.ok) {
            throw new Error("Unable to load guests");
        }

        const guests = await response.json();

        const table =
            document.getElementById("guestTableBody");

        table.innerHTML = "";

        guests.forEach(guest => {

            table.innerHTML += `
                <tr>

                    <td>${guest.id}</td>

                    <td>${guest.name}</td>

                    <td>${guest.email}</td>

                    <td>${guest.phone}</td>

                    <td>

                        <button
                            class="action-btn edit-btn"
                            onclick='editGuest(${JSON.stringify(guest)})'>
                            Edit
                        </button>

                        <button
                            class="action-btn delete-btn"
                            onclick="deleteGuest(${guest.id})">
                            Delete
                        </button>

                    </td>

                </tr>
            `;
        });

    } catch (error) {

        showMessage(error.message, "error");
    }
}


async function saveGuest() {

    const id =
        document.getElementById("guestId").value;

    const guest = {

        name:
        document.getElementById("guestName").value,

        email:
        document.getElementById("guestEmail").value,

        phone:
        document.getElementById("guestPhone").value
    };

    try {

        let response;

        if (id) {

            response = await fetch(
                `${API}/guests/${id}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(guest)
                }
            );

        } else {

            response = await fetch(
                `${API}/guests`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(guest)
                }
            );
        }

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Guest operation failed"
            );
        }

        showMessage(
            id
                ? "Guest updated successfully"
                : "Guest added successfully"
        );

        clearGuestForm();

        loadGuests();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


function editGuest(guest) {

    document.getElementById("guestId").value =
        guest.id;

    document.getElementById("guestName").value =
        guest.name;

    document.getElementById("guestEmail").value =
        guest.email;

    document.getElementById("guestPhone").value =
        guest.phone;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}


async function deleteGuest(id) {

    if (!confirm("Are you sure you want to delete this guest?")) {
        return;
    }

    try {

        const response =
            await fetch(`${API}/guests/${id}`, {
                method: "DELETE"
            });

        const message = await response.text();

        if (!response.ok) {
            throw new Error(message);
        }

        showMessage(message);

        loadGuests();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


function clearGuestForm() {

    document.getElementById("guestId").value = "";
    document.getElementById("guestName").value = "";
    document.getElementById("guestEmail").value = "";
    document.getElementById("guestPhone").value = "";
}


// ===============================
// BOOKINGS
// ===============================

async function loadBookings() {

    try {

        const response =
            await fetch(`${API}/bookings`);

        if (!response.ok) {
            throw new Error("Unable to load bookings");
        }

        const bookings = await response.json();

        const table =
            document.getElementById("bookingTableBody");

        table.innerHTML = "";

        bookings.forEach(booking => {

            const roomId =
                booking.room
                    ? booking.room.id
                    : "-";

            const guestName =
                booking.guest
                    ? booking.guest.name
                    : "-";

            const canCancel =
                booking.status === "CONFIRMED";

            table.innerHTML += `
                <tr>

                    <td>${booking.id}</td>

                    <td>${roomId}</td>

                    <td>${guestName}</td>

                    <td>${booking.checkInDate}</td>

                    <td>${booking.checkOutDate}</td>

                    <td>₹${booking.totalCost}</td>

                    <td>${booking.status}</td>

                    <td>

                        ${
                canCancel
                    ?
                    `
                            <button
                                class="action-btn cancel-btn"
                                onclick="cancelBooking(${booking.id})">
                                Cancel
                            </button>
                            `
                    :
                    "—"
            }

                    </td>

                </tr>
            `;
        });

    } catch (error) {

        showMessage(error.message, "error");
    }
}


async function createBooking() {

    const roomId =
        document.getElementById("bookingRoomId").value;

    const guestId =
        document.getElementById("bookingGuestId").value;

    const checkIn =
        document.getElementById("checkInDate").value;

    const checkOut =
        document.getElementById("checkOutDate").value;

    if (!roomId ||
        !guestId ||
        !checkIn ||
        !checkOut) {

        showMessage(
            "Please fill all booking details",
            "error"
        );

        return;
    }

    try {

        const url =
            `${API}/bookings` +
            `?roomId=${roomId}` +
            `&guestId=${guestId}` +
            `&checkInDate=${checkIn}` +
            `&checkOutDate=${checkOut}`;

        const response =
            await fetch(url, {
                method: "POST"
            });

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Booking failed"
            );
        }

        showMessage(
            `Booking created successfully. Total cost: ₹${data.totalCost}`
        );

        document.getElementById("bookingRoomId").value = "";
        document.getElementById("bookingGuestId").value = "";
        document.getElementById("checkInDate").value = "";
        document.getElementById("checkOutDate").value = "";

        loadBookings();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


async function cancelBooking(id) {

    if (!confirm("Cancel this booking?")) {
        return;
    }

    try {

        const response =
            await fetch(
                `${API}/bookings/${id}/cancel`,
                {
                    method: "PUT"
                }
            );

        const message =
            await response.text();

        if (!response.ok) {
            throw new Error(message);
        }

        showMessage(message);

        loadBookings();

    } catch (error) {

        showMessage(error.message, "error");
    }
}


// ===============================
// ROOM AVAILABILITY
// ===============================

async function checkAvailability() {

    const checkIn =
        document.getElementById(
            "availabilityCheckIn"
        ).value;

    const checkOut =
        document.getElementById(
            "availabilityCheckOut"
        ).value;

    if (!checkIn || !checkOut) {

        showMessage(
            "Please select check-in and check-out dates",
            "error"
        );

        return;
    }

    try {

        const url =
            `${API}/rooms/available` +
            `?checkInDate=${checkIn}` +
            `&checkOutDate=${checkOut}`;

        const response =
            await fetch(url);

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Unable to check availability"
            );
        }

        const table =
            document.getElementById(
                "availabilityTableBody"
            );

        table.innerHTML = "";

        if (data.length === 0) {

            table.innerHTML = `
                <tr>
                    <td colspan="4">
                        No rooms available for selected dates.
                    </td>
                </tr>
            `;

            return;
        }

        data.forEach(room => {

            table.innerHTML += `
                <tr>

                    <td>${room.id}</td>

                    <td>${room.roomType}</td>

                    <td>${room.capacity}</td>

                    <td>₹${room.ratePerNight}</td>

                </tr>
            `;
        });

    } catch (error) {

        showMessage(error.message, "error");
    }
}


// ===============================
// MONTHLY REPORT
// ===============================

async function getReport() {

    const year =
        document.getElementById("reportYear").value;

    const month =
        document.getElementById("reportMonth").value;

    if (!year || !month) {

        showMessage(
            "Please enter year and month",
            "error"
        );

        return;
    }

    try {

        const url =
            `${API}/reports/occupancy` +
            `?year=${year}&month=${month}`;

        const response =
            await fetch(url);

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(
                data.message || "Unable to generate report"
            );
        }

        document.getElementById(
            "reportResult"
        ).innerHTML = `

            <div class="report-item">
                <strong>Month:</strong>
                ${data.month}
            </div>

            <div class="report-item">
                <strong>Total Rooms:</strong>
                ${data.totalRooms}
            </div>

            <div class="report-item">
                <strong>Occupied Room Nights:</strong>
                ${data.occupiedRoomNights}
            </div>

            <div class="report-item">
                <strong>Total Room Nights:</strong>
                ${data.totalRoomNights}
            </div>

            <div class="report-item">
                <strong>Occupancy Percentage:</strong>
                ${data.occupancyPercentage.toFixed(2)}%
            </div>
        `;

    } catch (error) {

        showMessage(error.message, "error");
    }
}


// ===============================
// PAGE LOAD
// ===============================

window.addEventListener("DOMContentLoaded", () => {

    loadRooms();

});