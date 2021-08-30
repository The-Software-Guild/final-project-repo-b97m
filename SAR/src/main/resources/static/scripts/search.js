/* Canvas animation */
window.addEventListener("load", () => {
    function animate() {
	brush.fillStyle = "hsl(0, 0%, 8%)";
	brush.fillRect(0, 0, canv.width, canv.height);
	brush.fillStyle = "hsla(0, 0%, 8%, 0%)";
	brush.fillRect(0, 0, canv.width, canv.height);

	brush.strokeStyle = "hsl(0, 0%, 64%)";
	for (let i = 0; i < possX.length; i++) {
	    let dx = possX[i] - centX;
	    let dy = possY[i] - centY;
	    let dm = magnitude(dx, dy);
	    possX[i] += speed * dx / dm;
	    possY[i] += speed * dy / dm;

	    let dist = magnitude(dx ,dy);
	    brush.beginPath();
	    brush.arc(possX[i], possY[i], Math.sqrt(dist), 0, 2 * Math.PI);
	    brush.stroke();
	    if (2 * Math.abs(dx) > canv.width || 2 * Math.abs(dy) > canv.height) {
		let rad = 100 + (Math.random() * 50);
		let ang = Math.random() * Math.PI;
		possX[i] = centX + rad * Math.cos(ang);
		possY[i] = centY + rad * Math.sin(ang);
	    }
	}
	window.requestAnimationFrame(animate);
    }

    function magnitude(dx ,dy) {
	let retrVal = Math.sqrt(dx * dx + dy * dy);
	if (retrVal <= 0) {
	    retrVal = 1;
	}
	return retrVal;
    }

    window.addEventListener("resize", () => {
	centX = (canv.width = canv.clientWidth) / 2;
	centY = (canv.height = canv.clientHeight) / 2;
    });

    const speed = 3;
    const bubbles = 30;

    let canv, brush;
    let possX = [];
    let possY = [];
    let centX;
    let centY;

    canv = document.querySelector("canvas");
    centX = (canv.width = canv.clientWidth) / 2;
    centY = (canv.height = canv.clientHeight) / 2;

    brush = canv.getContext("2d");

    for (let i = 0; i < bubbles; i++) {
	let rad = 100 + (Math.random() * 50);
	let ang = Math.random() * Math.PI;
	possX[i] = centX + rad * Math.cos(ang);
	possY[i] = centY + rad * Math.sin(ang);
    }

    window.requestAnimationFrame(animate);

});


/* Search bar functionality */
let searchInput;
let searchButton;
let searchResults;
let searchErrors;
let resultsText;
const sampleResults = {
    "status": "ok",
    "totalResults": 6,
    "articles": [
        {
            "source": {
                "id": null,
                "name": "Github.io"
            },
            "author": null,
            "title": "Learning APL",
            "description": "Comments",
            "url": "https://xpqz.github.io/learnapl/intro.html",
            "urlToImage": null,
            "publishedAt": "2021-08-06T20:01:57Z",
            "content": "A language that doesnt affect the way you think about programming is not worth knowing.\r\nAlan Perlis\r\nWho is this for?¶\r\nI wrote this to be the book I would have wanted to read when I started to lear… [+17641 chars]"
        },
        {
            "source": {
                "id": null,
                "name": "Arraycast.com"
            },
            "author": null,
            "title": "The Array Cast: A podcast about the array programming languages",
            "description": "Comments",
            "url": "https://www.arraycast.com/episodes",
            "urlToImage": null,
            "publishedAt": "2021-07-29T10:22:19Z",
            "content": "There are lots of reasons to like the array languages APL, J and k/q! Listen and find out more."
        },
        {
            "source": {
                "id": null,
                "name": "Opensource.com"
            },
            "author": "Jen Wike Huger",
            "title": "What was your first programming language?",
            "description": "We asked our contributors What was your first programming language? but the question goes much deeper than that. There are stories to tell about who suggested it or what prompted you to learn it. If you were paid to do so, and what happened next. Then there's…",
            "url": "https://opensource.com/article/21/8/first-programming-language",
            "urlToImage": "https://opensource.com/sites/default/files/lead-images/computer_space_graphic_cosmic.png",
            "publishedAt": "2021-08-16T07:01:00Z",
            "content": "We asked our contributors What was your first programming language? but the question goes much deeper than that. There are stories to tell about who suggested it or what prompted you to learn it. If … [+27605 chars]"
        },
        {
            "source": {
                "id": null,
                "name": "Geeksforgeeks.org"
            },
            "author": "Neeraj sharma",
            "title": "Lexicographically smallest string with given string as prefix",
            "description": "Given an array arr[] consisting of N strings and a string S if size M, the task is to find the lexicographically smallest string consisting… Read More\nThe post Lexicographically smallest string with given string as prefix appeared first on GeeksforGeeks.",
            "url": "https://www.geeksforgeeks.org/lexicographically-smallest-string-with-given-string-as-prefix/",
            "urlToImage": "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png",
            "publishedAt": "2021-08-27T08:39:18Z",
            "content": "Given an arrayarr[] consisting of Nstrings and a string S if size M, the task is to find the lexicographically smallest string consisting of the string S as the prefix. If there doesn’t exist any str… [+2405 chars]"
        },
        {
            "source": {
                "id": null,
                "name": "Stackoverflow.blog"
            },
            "author": "Ben Popper and Juan M",
            "title": "Celebrating the Stack Exchange sites that turned 10 years old",
            "description": "We're more than just StackOverflow.com; here's a few of the sites in the Stack Exchange network that have been around for a decade!\nThe post Celebrating the Stack Exchange sites that turned 10 years old appeared first on Stack Overflow Blog.",
            "url": "https://stackoverflow.blog/2021/08/19/celebrating-the-stack-exchange-sites-that-turned-10-years-old/",
            "urlToImage": "https://149351115.v2.pressablecdn.com/wp-content/uploads/2021/08/se-10-year.png",
            "publishedAt": "2021-08-19T20:14:48Z",
            "content": "Its been 13 years since Stack Overflow was founded. It began with a simple mission, to serve as a question and answer site for professional and enthusiast programmers. Its become a place where anyone… [+9441 chars]"
        },
        {
            "source": {
                "id": null,
                "name": "Plos.org"
            },
            "author": "Mohammad Zohrul Islam, Mohammad Lutfi Othman, Noor Izzri Abdul Wahab, Veerapandiyan Veerasamy, Saifur Rahman Opu, Abinaya Inbamani, Vishalakshi Annamalai",
            "title": "Marine predators algorithm for solving single-objective optimal power flow",
            "description": "This study presents a nature-inspired, and metaheuristic-based Marine predator algorithm (MPA) for solving the optimal power flow (OPF) problem. The significant insight of MPA is the widespread foraging strategy called the Levy walk and Brownian movements in …",
            "url": "https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0256050",
            "urlToImage": "https://journals.plos.org/plosone/article/figure/image?id=10.1371/journal.pone.0256050.g009&size=inline",
            "publishedAt": "2021-08-12T14:00:00Z",
            "content": "Abstract\r\nThis study presents a nature-inspired, and metaheuristic-based Marine predator algorithm (MPA) for solving the optimal power flow (OPF) problem. The significant insight of MPA is the widesp… [+54713 chars]"
        }
    ]
};

window.addEventListener("load", () => {

    searchInput = document.querySelector("input");
    searchButton = document.querySelector("button");
    searchResults = document.querySelector("#search-results");
    searchErrors = document.querySelector("#search-errors");
    
    function processQuery() {
	let queryStr = encodeURIComponent(searchInput.value);
	searchErrors.innerHTML = "";

	console.log("querying: " + queryStr);
	if (queryStr.length > 0 && !searchButton.hasAttribute("disabled")) {
	    searchButton.setAttribute("disabled", "");

	    customAjaxPost(
		"http://localhost:8080/sar-aux/request", 
		{text: queryStr}
	    ).then(res => { 
		console.log(res);
		return customAjaxGet("http://localhost:8080/sar-aux/request");
	    }).then(res => {
		console.log(res);
		searchInput.value = "";
		searchInput.setAttribute("placeholder", `${100 - res} request(s) remaining`);		
	    	return customAjaxGet(`http://localhost:8080/sar-secrets/articles/${queryStr}`);
	    }).then(res => {
		console.log("results: " + (resultsText = res));
		processResults(resultsText);
		setTimeout(
		    () => searchButton.removeAttribute("disabled"), 
		    500
		);
	    }).catch(err => {
		console.log(err);
		searchInput.value = "";
		setTimeout(
		    () => searchButton.removeAttribute("disabled"), 
		    500
		);
		$(searchErrors).append(`<p>${err}</p>`);
	    });
	}
    }

    searchButton.addEventListener("mousedown", evt => processQuery());
    searchInput.addEventListener("keydown", evt => {
	if (evt.key === "Enter") {
	    processQuery();
	}
    });
    searchButton.addEventListener("keydown", evt => {
	if (evt.key === "Enter") {
	    processQuery();
	}
    });

    customAjaxGet("http://localhost:8080/sar-aux/request")
    .then(res => {
	searchInput.setAttribute("placeholder", `${100 - res} request(s) remaining`);
    }).catch(err => {
	console.log(err);
    });
});

    function customAjaxGet(url) {
	return new Promise((resolver, rejector) => {
	    $.ajax(url).done(res => resolver(res)).fail(err => rejector(err));
	});
    }

    function customAjaxPost(url, data) {
	return new Promise((resolver, rejector) => {
	    $.ajax(
		url, 
		{
		    method: "POST", 
		    data: JSON.stringify(data), 
		    contentType: "application/json; charset=utf-8"
		}
	    ).done(res => resolver(res))
	    .fail(err => rejector(err));
	});
    }

    function processResults(resultsText) {
	let resultsJson = null;
	try {
	    resultsJson = JSON.parse(resultsText);
	} catch (err) {
	    console.log(err);
	}
	    
	if (resultsJson !== null) {
	    const { status, articles } = resultsJson;
	    if (status === "ok") {
		renderArticles(articles);
	    } else {
		$(searchErrors).append(`<p>Something went wrong while retrieving the search results</p>`);
	    }
	} else {
	    $(searchErrors).append(`<p>Something went wrong while retrieving the search results</p>`);
	}
    }

    function renderArticles(articles) {
	searchResults.innerHTML = "";
	articles.forEach(article => {
	    let item = document.createElement("div");
	    let itemTitle = document.createElement("h2");
	    itemTitle.innerText = article.title;

	    let itemAuthorSource = document.createElement("h3");
	    itemAuthorSource.innerText = `${article.author} | ${article.source.name} | ${article.publishedAt}`;

	    let itemReadLink = document.createElement("a");
	    itemReadLink.setAttribute("href", `${article.url}`);
	    itemReadLink.setAttribute("target", "_blank");
	    itemReadLink.setAttribute("rel", "noopener noreferrer");
	    itemReadLink.innerText = "Read More";

	    let itemSaveButton = document.createElement("button");
	    itemSaveButton.innerText = "Save For Review";
	    itemSaveButton.addEventListener("click", evt => {
		customAjaxPost(
		    "http://localhost:8080/sar-aux/article", 
		    {
			author: article.author,
			title: article.title,
			url: article.url,
			publicationTime: article.publishedAt,
			sourceName: article.source.name
		    }
		).then(res => {
		    console.log(res);
	    	    searchResults.removeChild(evt.target.parentElement);
		})
		.catch(err => {
		    console.log(err);
	    	    $(searchErrors).append("<p>Something went wrong with saving the article</p>");
		});
	    });

    	    item.appendChild(itemTitle);
	    item.appendChild(itemAuthorSource);
	    item.appendChild(itemReadLink);
	    item.appendChild(itemSaveButton);
	    searchResults.appendChild(item);
	});
    }