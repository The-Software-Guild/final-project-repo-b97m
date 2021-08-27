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
let results;
window.addEventListener("load", () => {
    searchInput = document.querySelector("input");
    searchButton = document.querySelector("button");

    function processQuery() {
	let queryStr = encodeURIComponent(searchInput.value);
	console.log("querying: " + queryStr);
	if (queryStr.length > 0 && !searchButton.hasAttribute("disabled")) {
	    console.log("processing...");
	    searchButton.setAttribute("disabled", "");
	    $.ajax(`http://localhost:8080/sar-secrets/articles/${queryStr}`)
	    .done(res => {
		console.log("results: " + (results = res));
		setTimeout(
		    () => searchButton.removeAttribute("disabled"), 
		    500
		);
	    }).fail(err => console.log(err));
	}
    }

    searchButton.addEventListener("mousedown", evt => {
	processQuery();
    });

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
});