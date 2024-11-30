const newsContainer = document.querySelector('.news-container');
database = []

function initNews() {
    for (let news of database) {
        const newsItem = document.createElement('div');
        newsItem.classList.add('news-item');

        const newsTitle = document.createElement('h3');
        newsTitle.textContent = news.title;
        const newsTag = document.createElement('p');
        newsTag.textContent = news.tag;

        const newsLink = document.createElement('a');
        newsLink.href = news.url;
        newsLink.textContent = 'Узнать больше...';

        newsItem.appendChild(newsTitle);
        newsItem.appendChild(newsTag);
        newsItem.appendChild(newsLink);

        newsContainer.appendChild(newsItem);
    }
}

function initialization() {
    getNews().then(() => {
        initNews();
    });
}
initialization();

function getNews() {
    return new Promise((resolve) => {
        fetch(`/api/news`)
            .then(response => response.json())
            .then(gettedNews => {
                database = []
                for (let item of gettedNews) {
                    database.push({
                        title: item.title,
                        url: item.url,
                        tag: item.tag
                    })
                }
                console.log("Новости: ", gettedNews);
                resolve();
            });
    })
}

