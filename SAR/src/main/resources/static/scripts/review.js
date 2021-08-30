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

window.addEventListener("load", () => {
    let savedArticles = document.querySelector("#saved-articles");

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
    
    customAjaxGet("http://localhost:8080/sar-aux/article")
    .then(articles => {
	console.log(articles);
	articles.forEach(article => {
	    let item = document.createElement("div");
	    let itemTitle = document.createElement("h2");
	    itemTitle.innerText = article.title;

	    let itemAuthorSource = document.createElement("h3");
	    itemAuthorSource.innerText = `${article.author} | ${article.sourceName} | ${article.publishedAt}`;

	    let itemReadLink = document.createElement("a");
	    itemReadLink.setAttribute("href", `${article.url}`);
	    itemReadLink.setAttribute("target", "_blank");
	    itemReadLink.setAttribute("rel", "noopener noreferrer");
	    itemReadLink.innerText = "Read More";

	    let itemReviewButton = document.createElement("button");
	    itemReviewButton.innerText = "Make Review";

    	    item.appendChild(itemTitle);
	    item.appendChild(itemAuthorSource);
	    item.appendChild(itemReadLink);
	    item.appendChild(itemReviewButton);
	    savedArticles.appendChild(item);
	});
    }).catch(err => {
	console.log(err);
    });
});