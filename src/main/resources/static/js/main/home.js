const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const bookResultContainer = document.getElementById('book-result-container');
const heartAllBtn = document.getElementById('heart-all-btn');
const cartAllBtn = document.getElementById('cart-all-btn');
const searchInputDiv = document.getElementById('search-input');
const searchInput = searchInputDiv.querySelector('input');
const searchBtn = searchInputDiv.lastElementChild;
const searchResultOrderSelect = document.getElementById('search-result-order-select');
const searchResultCountSelect = document.getElementById('search-result-count-select');
const bookPagingList = document.getElementById('book-paging-list');

//처음 접속 시 책 리스트를 생성함
get_books();

// 책 찜하기/장바구니 넣기 공통 처리 부분
// (버튼 눌렀을 시 => 데이터 생성, 가능여부 체크)
function insert_heart_cart_checked(userEmail, clickedBtn){
    // 로그인이 안되어있다
    if(!userEmail){
        alert('로그인부터 하세요^-^');
    }else{
        const bookISBNArray = [];
        const bookInfoContainer = document.getElementsByClassName('book-info-container');
        for(container of bookInfoContainer){
            const bookInfoCheck = container.getElementsByClassName('book-info-check').item(0);
            //체크박스가 선택되어 있다면
            if(bookInfoCheck.checked){
                const bookInfoISBN = container.getElementsByClassName('book-info-isbn').item(0);
                bookISBNArray.push({ userEmail: userEmail, bookISBN : bookInfoISBN.value });
            }
        }
        //내가 넣은 배열에 아무것도 들어있지 않다면
        if(bookISBNArray.length === 0){
            alert('상품을 하나라도 선택해주세요!');
        }else{
            insert(clickedBtn, bookISBNArray);
        }
    }
}

// 책을 장바구니에 넣거나 / 혹은 찜 하는 메소드
function insert(clickedBtn, bookISBNArray){
    const requestURL = clickedBtn === heartAllBtn ? '/user/heart' : '/user/cart';
    fetch(requestURL, {
            method: 'POST',
            headers: {
                "content-Type": 'application/json',
                "X-CSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(bookISBNArray)
        })
        .then(value => value.text())
        .then(value => { console.log(value); })
        .catch(reason => { console.log(reason); });
}

// 모든 책의 정보를 가져오는 메소드
function get_books(){
    fetch('/main/books')
        .then(response => response.json())
        .then(value => { create_book_list(value); })
        .catch();
}

// 키워드를 통해 책들 정보 받아오기
function get_all_books_by_condition(searchKeyword, order, pagePerArticle, nowPage){
    let URL =  `/main/books`;  //기본 URL
    URL += '?order=' + order;                            //순서 조건
    URL += '&pagePerArticle=' + pagePerArticle;          //페이지당 보여줄 개수
    URL += '&nowPage=' + nowPage;                        //현재 페이지 번호
    URL = searchKeyword === '' ? URL : URL + '&searchKeyword=' + searchKeyword;  //검색키워드

    fetch(URL)
        .then(value => value.json())
        .then(value => {
            create_book_list(value);
            create_list_of_paging(value);
        })
        .catch(reason => {})
}

// 책 리스트를 생성하는 메소드
function create_book_list(bookObject){
    bookResultContainer.innerHTML = '';
    for(book of bookObject.bookVOS){
        bookResultContainer.insertAdjacentHTML('beforeend',
            '<div class="book-info-container">\n' +
            `<input type="hidden" class="book-info-isbn" value="${book.isbn}">` +
            '                <span><input class="book-info-check" type="checkbox"></span>\n' +
            '                <img src="" alt="상품이미지">\n' +
            `                <a href="/main/details/${book.isbn}" class="book-info-title">${book.title}</a>\n` +
            '                <span>\n' +
            `                    <a href="#">${book.author}</a>\n` +
            '                     *\n' +
            `                    <a href="#">${book.publisher}</a>\n` +
            '                </span>\n' +
            `                <span>${book.price}원</span>\n` +
            '                <span><input class="book-info-heart" type="button" value="하트"></span>\n' +
            '            </div>');
    }
}

// 전체 페이지 번호 리스트를 생성하는 메소드
function create_list_of_paging(bookObject){
    console.log(bookObject)
    bookPagingList.innerHTML = '';
    for(let i = bookObject.minPage; i < bookObject.maxPage; i++){
        if(i === bookObject.nowPage){
            bookPagingList.insertAdjacentHTML('beforeend',
                `<li onclick="book_page_li_clicked(this)"><b>${i}</b></li></>`
            );
        }else {
            bookPagingList.insertAdjacentHTML('beforeend',
                `<li onclick="book_page_li_clicked(this)">${i}</li>`
            );
        }
    }
}

// 키워드 입력 후 엔터 눌렀을 시 동작
searchInput.onkeydown = event => {
    if(event.key === 'Enter'){
        if(searchInput.value.trim() === ''){
            alert('검색어를 입력하세요!');
            //모든 책 리스트를 가져오도록 함
            get_books();
        }else{
            const keyword = searchInput.value;              //키워드
            const order =  searchResultOrderSelect.value    //정렬순서
            const pagePerArticle = searchResultCountSelect.value;   //한페이지당 개수
            //검색어를 제대로 입력하고 엔터키 눌렀을 시 서버에 해당 데이터를 요청함
            get_all_books_by_condition(keyword, order, pagePerArticle, 1);
        }
    }
}

// 정렬 순서 셀렉 박스 클릭 시 동작
searchResultOrderSelect.onchange = () => {
    const keyword = searchInput.value;              //키워드
    const order =  searchResultOrderSelect.value    //정렬순서
    const pagePerArticle = searchResultCountSelect.value;   //한페이지당 개수
    //검색어를 제대로 입력하고 엔터키 눌렀을 시 서버에 해당 데이터를 요청함
    get_all_books_by_condition(keyword, order, pagePerArticle, 1);
}

// 표시 개수 셀렉 박스 클릭 시 동작
searchResultCountSelect.onchange = () => {
    const keyword = searchInput.value;              //키워드
    const order =  searchResultOrderSelect.value    //정렬순서
    const pagePerArticle = searchResultCountSelect.value;   //한페이지당 개수
    //검색어를 제대로 입력하고 엔터키 눌렀을 시 서버에 해당 데이터를 요청함
    get_all_books_by_condition(keyword, order, pagePerArticle, 1);
}

// 페이지 번호 눌렀을 때 동작 (누른 li)
function book_page_li_clicked(li){
    const keyword = searchInput.value;              //키워드
    const order =  searchResultOrderSelect.value    //정렬순서
    const pagePerArticle = searchResultCountSelect.value;   //한페이지당 개수
    const nowPage = li.textContent;
    get_all_books_by_condition(keyword, order, pagePerArticle, nowPage);
}








