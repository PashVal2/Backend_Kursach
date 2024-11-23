document.addEventListener("DOMContentLoaded", () => {
    const calendarDates = document.querySelector(".calendar-dates");
    const monthYear = document.querySelector(".month-year");
    const prevMonthBtn = document.querySelector(".prev-month");
    const nextMonthBtn = document.querySelector(".next-month");

    let currentDate = new Date();

    function renderCalendar(date) {
        const year = date.getFullYear();
        const month = date.getMonth();

        // Get the first and last day of the month
        const firstDayOfMonth = new Date(year, month, 1);
        const lastDayOfMonth = new Date(year, month + 1, 0);

        // Determine the first day to display in the calendar
        const startDay = firstDayOfMonth.getDay() === 0 ? 6 : firstDayOfMonth.getDay() - 1;

        // Clear the previous dates
        calendarDates.innerHTML = "";

        // Set the header
        const monthNames = [
            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        ];
        monthYear.textContent = `${monthNames[month]} ${year}`;

        // Add blank days for the previous month
        for (let i = 0; i < startDay; i++) {
            const blankDay = document.createElement("div");
            blankDay.classList.add("date", "empty");
            calendarDates.appendChild(blankDay);
        }

        // Add days of the current month
        for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
            const dateElement = document.createElement("div");
            dateElement.classList.add("date");
            dateElement.textContent = day;
            calendarDates.appendChild(dateElement);

            // Add click event for selection
            dateElement.addEventListener("click", () => {
                dateElement.classList.add("selected");
            });
        }
    }

    function changeMonth(offset) {
        currentDate.setMonth(currentDate.getMonth() + offset);
        renderCalendar(currentDate);
    }

    // Initialize the calendar
    renderCalendar(currentDate);

    // Add event listeners for navigation buttons
    prevMonthBtn.addEventListener("click", () => changeMonth(-1));
    nextMonthBtn.addEventListener("click", () => changeMonth(1));


});
