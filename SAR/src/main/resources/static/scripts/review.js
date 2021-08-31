let subjectArticle = null;
let subjectReview = null;

window.addEventListener("load", () => {
    let savedArticles = document.querySelector("#saved-articles");

    let deleteConfirm = document.querySelector("#delete-confirm");

    let articleReviewsHeader = document.querySelector("#article-reviews-header");

    let articleReviewsList = document.querySelector("#article-reviews-list");

    let articleReviewsCreationForm = document.querySelector("#article-reviews-creation-form");
    let articleReviewsCreationTextarea = articleReviewsCreationForm.children[0];
    let articleReviewsCreationSubmit = articleReviewsCreationForm.children[1];
    let articleReviewsCreationErrors = articleReviewsCreationForm.children[2];
    articleReviewsCreationSubmit.addEventListener("click", evt => {
	evt.preventDefault();
	if (subjectArticle !== null) {
	    customAjaxPost("http://localhost:8080/sar-aux/review", {
	    	text: articleReviewsCreationTextarea.value,
		articleId: subjectArticle.id
	    })
	    .then(res => {
		articleReviewsCreationTextarea.value = "";
		articleReviewsCreationErrors.innerText = "";
		reloadReviewsFor(subjectArticle)();
	    })
	    .catch(err => {
		console.log(err);
		articleReviewsCreationErrors.innerText = `
		    Either the review was too long,(${articleReviewsCreationTextarea.value.length}/1000 chars),
		    or something went wrong with creating the review
		`;
	    });
	}
    });

    let articleReviewsEditForm = document.querySelector("#article-reviews-edit-form");
    let articleReviewsEditTextarea = articleReviewsEditForm.children[0];
    let articleReviewsEditSubmit = articleReviewsEditForm.children[1];
    let articleReviewsEditCancel = articleReviewsEditForm.children[2];
    let articleReviewsEditErrors = articleReviewsEditForm.children[3];
    articleReviewsEditSubmit.addEventListener("click", evt => {
	evt.preventDefault();
    	if (subjectArticle !== null && subjectReview !== null) {
	    customAjaxPut("http://localhost:8080/sar-aux/review", {
	    	text: articleReviewsEditTextarea.value,
		id: subjectReview.id
	    }).then(() => {
		articleReviewsEditTextarea.value = "";
		articleReviewsEditErrors.innerText = "";
		reloadReviewsFor(subjectArticle)();
		$(articleReviewsEditForm).hide();
		$(articleReviewsCreationForm).show();
	    }).catch(err => {
		console.log(err);
		articleReviewsEditErrors.innerText = `
		    Either the review was too long,(${articleReviewsEditTextarea.value.length}/1000 chars),
		    or something went wrong with saving the edit
		`;
	    });
	}
    });
    articleReviewsEditCancel.addEventListener("click", evt => {
	evt.preventDefault();
	$(articleReviewsEditForm).hide();
	$(articleReviewsCreationForm).show();
    });

    $(deleteConfirm).hide();
    $(articleReviewsHeader).hide();
    $(articleReviewsList).hide();
    $(articleReviewsCreationForm).hide();
    $(articleReviewsEditForm).hide();

    function customAjaxGet(url) {
	return new Promise((resolver, rejector) => {
	    $.ajax(url).done(res => resolver(res)).fail(err => rejector(err));
	});
    }

    function customAjaxNonGet(method) {
	return (url, data) => {
	    return new Promise((resolver, rejector) => {
		$.ajax(
		    url, 
		    {
			method: method, 
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8"
		    }
		).done(res => resolver(res))
		.fail(err => rejector(err));
	    });
	};
    }
    const customAjaxPost = customAjaxNonGet("POST");
    const customAjaxPut = customAjaxNonGet("PUT");
    const customAjaxDelete = customAjaxNonGet("DELETE");

    function reloadSavedArticles() {
	customAjaxGet("http://localhost:8080/sar-aux/article")
	.then(articles => {
	    savedArticles.innerHTML = "";
	    articles.forEach(article => {
		let item = document.createElement("div");
		let itemTitle = document.createElement("h2");
		itemTitle.innerText = article.title;

		let itemAuthorSource = document.createElement("h3");
		itemAuthorSource.innerText = `${article.author} | ${article.sourceName} | ${article.publicationTime} UTC`;

		let itemReadLink = document.createElement("a");
		itemReadLink.setAttribute("href", `${article.url}`);
		itemReadLink.setAttribute("target", "_blank");
		itemReadLink.setAttribute("rel", "noopener noreferrer");
		itemReadLink.innerText = "Read More";

		let itemReviewButton = document.createElement("button");
		itemReviewButton.innerText = "Review";
		itemReviewButton.addEventListener("click", reloadReviewsFor(article));

		let itemDeletionButton = document.createElement("button");
		itemDeletionButton.innerText = "Delete"; 
		itemDeletionButton.addEventListener("click", handleArticleDeleteFor(article));

		item.appendChild(itemTitle);
		item.appendChild(itemAuthorSource);
		item.appendChild(itemReadLink);
		item.appendChild(itemReviewButton);
		item.appendChild(itemDeletionButton);

		savedArticles.appendChild(item);
	    });
	}).catch(err => {
	    console.log(err);
	});
    }

    function reloadReviewsFor(article) {
    	return evt => {
	    $(articleReviewsHeader).show();
	    $(articleReviewsCreationForm).show();
	    $(articleReviewsList).show();

	    subjectArticle = article;
	    articleReviewsList.innerHTML = "";
	    articleReviewsHeader.innerText = `Reviews for: ${article.title}`;
	    customAjaxGet(`http://localhost:8080/sar-aux/review/article/${article.id}`)
	    .then(reviews => {
		reviews.forEach(review => {
		    let reviewItem = document.createElement("div");
		    reviewItem.setAttribute("class", "article-review-item");
		    
		    let reviewHeader = document.createElement("h2");
		    reviewHeader.innerText = `ReviewId: ${review.id}`;

		    let creationTimestamp = document.createElement("p");
		    creationTimestamp.innerText = `Created: ${review.creationTime} UTC`;
		    let updateTimestamp = document.createElement("p");
		    updateTimestamp.innerText = `Updated: ${review.updateTime} UTC`;

		    let reviewText = document.createElement("p");
		    reviewText.innerText = review.text;
		    
		    let editButton = document.createElement("button");
		    editButton.innerText = "Edit";
		    editButton.addEventListener("click", handleReviewEditFor(review)); 

		    let deleteButton = document.createElement("button");
		    deleteButton.innerText = "Delete";
		    deleteButton.addEventListener("click", handleReviewDeleteFor(review));

		    reviewItem.appendChild(reviewHeader);
		    reviewItem.appendChild(creationTimestamp);
		    reviewItem.appendChild(updateTimestamp);
		    reviewItem.appendChild(reviewText);
		    reviewItem.appendChild(editButton);
		    reviewItem.appendChild(deleteButton);

		    articleReviewsList.appendChild(reviewItem); 
		});
	    }).catch(err => console.log(err));
	};
    }

    function handleReviewEditFor(review) {
	return evt => {
	    $(articleReviewsCreationForm).hide();
	    $(articleReviewsEditForm).show();
	    subjectReview = review;
	    articleReviewsEditTextarea.value = review.text;
	};
    }

    function handleReviewDeleteFor(review) {
	return evt => {
	    customAjaxDelete("http://localhost:8080/sar-aux/review", {
		id: review.id
	    })
	    .then(res => {
	    	if (subjectArticle !== null) {
		    reloadReviewsFor(subjectArticle)();
		    $(articleReviewsEditForm).hide();
		    $(articleReviewsCreationForm).show();
		}
	    })
    	    .catch(err => console.log(err));
	};
    }

    function handleArticleDeleteFor(article) {
   	return evt => {
	    customAjaxDelete("http://localhost:8080/sar-aux/article", {
	    	id: article.id
	    }).then(res => {
	    	if (subjectArticle !== null && subjectArticle.id === article.id) {
		    subjectArticle = null;
		    $(articleReviewsCreationForm).hide();
		    $(articleReviewsEditForm).hide();
		    $(articleReviewsHeader).hide();
		    $(articleReviewsList).hide();
		}
		reloadSavedArticles();
	    }).catch(err => console.log(err));
	}; 
    }

    reloadSavedArticles();
});