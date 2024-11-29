let databaseCoord = [];

async function initMap() {
    await ymaps3.ready;

    const {YMap, YMapDefaultSchemeLayer, YMapMarker, YMapDefaultFeaturesLayer} = ymaps3;

    // Создаем карту
    const map = new YMap(
        document.getElementById('map'),
        {
            location: {
                center: [37.588144, 55.733842], // Центр карты
                zoom: 10 // Уровень приближения
            }
        }
    );

    // Добавляем схематический слой на карту
    map.addChild(new YMapDefaultSchemeLayer());

    // Создаем и добавляем слой для объектов
    const featuresLayer = new YMapDefaultFeaturesLayer();
    map.addChild(featuresLayer);
    console.log("Координаты: ", databaseCoord);
    for (let item of databaseCoord) {
        const markerElement = document.createElement('div');
        markerElement.className = 'marker-class';

        const link = document.createElement('a');
        link.href = `/property/${item.name}_${item.id}`; // Set the link dynamically based on the item or a fixed URL
        markerElement.appendChild(link);

        // Создаем точку маркера
        const markerDot = document.createElement('div');
        markerDot.className = 'marker-dot';
        link.appendChild(markerDot);

        // Создаем текст маркера
        const markerText = document.createElement('div');
        markerText.className = 'marker-text';
        markerText.innerText = item.name;
        link.appendChild(markerText);

        const marker = new YMapMarker(
            {
                coordinates: [item.latitude, item.longitude],
                mapFollowsOnDrag: true
            },
            markerElement
        );
        // Добавляем маркер на слой
        featuresLayer.addChild(marker);
    }
}

// Инициализация карты
function initializeCalendar() {
    getCoord().then(() => {
        initMap();
    });
}
initializeCalendar();

function getCoord() {
    return new Promise((resolve) => {
        fetch(`/api/coord`)
            .then(response => response.json())
            .then(gettedCoord => {
            databaseCoord = [];
            for (let item of gettedCoord) {
                databaseCoord.push({
                    name: item.name,
                    latitude: item.latitude,
                    longitude: item.longitude,
                    id: item.id
                });
            }
            console.log("Координаты: ", gettedCoord);
            resolve();
        });
    })
}