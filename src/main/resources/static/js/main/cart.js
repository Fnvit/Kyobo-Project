const bookCartContainer = document.getElementById('book-cart-container');

get_cart();

function get_cart(){
    fetch('/user/cart')
        .then(response => response.json())
        .then(value => { create_book_list_in_cart(value); });
}

function create_book_list_in_cart(cartList){
    bookCartContainer.innerHTML = '';
    for(cart of cartList){
        bookCartContainer.insertAdjacentHTML('beforeend',
        `<section class="book-info-container">
                <i class="fa-regular fa-circle-check"></i>
                <img src="" alt="이미지">
                <div class="book-info">
                    <div>${cart.title}</div>
                    <span><b>${cart.price}</b>원</span>
                </div>
                <div class="book-info-price-container">
                    <div><b>${cart.price * cart.bookCount}</b>원</div>
                    <div class="book-info-count-container">
                        <i class="fa-solid fa-minus"></i>
                        <input type="number" value="${cart.bookCount}">
                        <i class="fa-solid fa-plus"></i>
                    </div>
                </div>
            </section>`)
    }
}







