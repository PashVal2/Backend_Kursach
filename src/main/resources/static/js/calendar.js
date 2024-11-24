const calendarDates = document.querySelector(".calendar-dates");
const monthYear = document.querySelector(".month-year");
const prevMonthBtn = document.querySelector(".prev-month");
const nextMonthBtn = document.querySelector(".next-month");

let currentDate = new Date();
const selectedDates = [];

const url = window.location.href;
const pathParts = url.split('/');
const propertyId = pathParts[pathParts.length - 1].split('_')[1];

function renderCalendar(date) {
    const year = date.getFullYear();
    const month = date.getMonth();

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
        dateElement.classList.add("date");
        dateElement.textContent = day;
        calendarDates.appendChild(dateElement);

        dateElement.addEventListener("click", () => {
            const day = parseInt(dateElement.textContent); // Парсим текст элемента как число
            const year = currentDate.getFullYear();
            const month = currentDate.getMonth();
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

function changeMonth(offset) {
    currentDate.setMonth(currentDate.getMonth() + offset);
    renderCalendar(currentDate);
}

renderCalendar(currentDate);

prevMonthBtn.addEventListener("click", () => changeMonth(-1));
nextMonthBtn.addEventListener("click", () => changeMonth(1));

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
    .then(data => {
        console.log('Загрузка ', data);
    })
}

function getDates() {
    fetch(`/api/book-dates/${propertyID}`)
    .then(response => response.json())
    .then(data => {
        console.log("Забронированные даты: ", data);
    });
}


