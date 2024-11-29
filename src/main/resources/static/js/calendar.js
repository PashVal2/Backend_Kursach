const calendarDates = document.querySelector(".calendar-dates");
const monthYear = document.querySelector(".month-year");
const prevMonthBtn = document.querySelector(".prev-month");
const nextMonthBtn = document.querySelector(".next-month");

let currentDate = new Date();
const realDate = new Date();
const selectedDates = [];

const url = window.location.href;
const pathParts = url.split('/');
const propertyId = pathParts[pathParts.length - 1].split('_')[1];

let databaseDate = [];

function renderCalendar(date) {
    const year = date.getFullYear();
    const month = date.getMonth();

    if (realDate.getFullYear() === date.getFullYear()
        && realDate.getMonth() === date.getMonth()) {
            prevMonthBtn.style.visibility = "hidden";
            // prevMonthBtn.style.pointerEvents = "none";
    }
    else {
        prevMonthBtn.style.visibility = "visible";
    }

    if ((realDate.getFullYear() + 1) === date.getFullYear()
    && realDate.getMonth() === date.getMonth()) {
        nextMonthBtn.style.visibility = "hidden";
    }
    else {
        nextMonthBtn.style.visibility = "visible";
    }

    const firstDayOfMonth = new Date(year, month, 1);
    const lastDayOfMonth = new Date(year, month + 1, 0);

    // первый
    let startDay;
    if (firstDayOfMonth.getDay() === 0) {
        startDay = 6;
    } else {
        startDay = firstDayOfMonth.getDay() - 1;
    }

    // Убираем предыдущие даты
    calendarDates.innerHTML = "";

    const monthNames = [
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    ];
    monthYear.textContent = `${monthNames[month]} ${year}`;

    // Пустые дни в начале месяца
    for (let i = 0; i < startDay; i++) {
        const blankDay = document.createElement("div");
        blankDay.classList.add("date", "empty");
        calendarDates.appendChild(blankDay);
    }

    // дни
    for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
        const dateElement = document.createElement("div");
        dateElement.textContent = day;
        calendarDates.appendChild(dateElement);
        const currentDay = new Date(year, month, day);
        const bookingInfo = isDateBooked(currentDay);
        
        if (bookingInfo) {
            if (bookingInfo.userIsOwner) {
                dateElement.classList.add("ownerBooked");
            }
            else {
                dateElement.classList.add("booked");
            }
        }
        else {
            dateElement.classList.add("date");
            dateElement.addEventListener("click", () => {
                const dateKey = `${year}-${month}-${day}`; // Используем правильную интерполяцию строк
                // Используем toggle для изменения класса и сохраняем результат (true - добавлено, false - удалено)
                if (dateElement.classList.toggle("selected")) {
                    // Если добавили класс "selected", добавляем дату в массив
                    selectedDates.push({ propertyId, year, month, day });
                } else {
                    // Если убрали класс "selected", удаляем дату из массива
                    const i = selectedDates.findIndex(
                        (date) => `${date.year}-${date.month}-${date.day}` === dateKey
                    );
                    if (i !== -1) selectedDates.splice(i, 1); // Удаляем элемент по индексу
                }
                console.log(selectedDates);
            });
        }
    }
}

function changeMonth(offset) {
    const newDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + offset, 1);
    console.log("Меняем месяц на:", newDate);
    currentDate = newDate;
    initializeCalendar();
}

function initializeCalendar() {
    getDates().then(() => {
        renderCalendar(currentDate);
    });
}
initializeCalendar(currentDate);

function submitDates() {
    console.log("Выбранные даты для бронирования:", selectedDates);
    fetch('/api/book-dates', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(selectedDates)
    })
    .then(response => response.json())
    .then(responseData => {
        initializeCalendar(currentDate);
        console.log('Загрузка ', responseData);
    })
}

function getDates() {
    return new Promise((resolve) => {
        fetch(`/api/book-dates/${propertyId}`)
        .then(response => response.json())
        .then(gettedData => {
            databaseDate = [];
            for (let item of gettedData) {
                databaseDate.push({
                    date: new Date(item.year, item.month, item.day), // Исправлено: `month - 1` для корректного месяца
                    userIsOwner: item.userIsOwner
                });
            }
            console.log("Забронированные даты: ", gettedData);
            resolve();
        });
    })
}

function isDateBooked(compareDate) {
    for (let item of databaseDate) {
        if (item.date.getFullYear() === compareDate.getFullYear() &&
            item.date.getMonth() === compareDate.getMonth() &&
            item.date.getDate() === compareDate.getDate()) {
            return item;
        }
    }
    return null;
}

