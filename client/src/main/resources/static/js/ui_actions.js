let account = window.initialAccount || {};
if (!account.incomes) account.incomes = []; // Safety check
if (!account.expenses) account.expenses = []; // Safety check


const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;



function addItem(type){
    const modalId = type === 'income' ? 'income':'expense';

    if(modalId === 'income'){
        console.log('income')

        const amount = parseFloat(document.getElementById("income-amount-input").value);
        const currency = document.getElementById("income-currency-select").value;
        const period = document.getElementById("income-type-select").value;
        const icon = document.getElementById("incomeIconInput").value;
        const title = document.querySelector('input[title="title-income-input"]').value;

        const newItem = {
            title,
            amount,
            currency,
            period,
            icon
        };

        account.incomes.push(newItem);
        toggleModal('income',false)

        console.log(newItem)
        console.log(account)
    }else if(modalId === 'expense'){
        console.log('expense')

        const amount = parseFloat(document.getElementById("expense-amount-input").value);
        const currency = document.getElementById("expense-currency-select").value;
        const period = document.getElementById("expense-type-select").value;
        const icon = document.getElementById("expenseIconInput").value;
        const title = document.querySelector('input[title="title-expense-input"]').value;

        const newItem = {
            title,
            amount,
            currency,
            period,
            icon
        };

        account.expenses.push(newItem);
        toggleModal('expense',false)

        console.log(newItem)
        console.log(account)

    }




}


function removeIncomeItem(button){
    const id = button.dataset.id;
    const item = document.getElementById(id);
    if (item) item.remove();
    if(window.initialAccount.incomes && Array.isArray(window.initialAccount.incomes)){
        window.initialAccount.incomes = window.initialAccount.incomes.filter((income) => income.id !== id);
    }
    console.log("Removed incomes with id:", id);
}
function removeExpenseItem(button){
    const id = button.dataset.id;
    const item = document.getElementById(id);
    if (item) item.remove();
    if(window.initialAccount.expenses && Array.isArray(window.initialAccount.expenses)){
        window.initialAccount.expenses = window.initialAccount.expenses.filter((expenses) => expenses.id !== id);
    }
    console.log("Removed expenses with id:", id);
}


function toggleModal(type, open) {
    const modalId = type === 'income' ? 'incomeModal' : 'expenseModal';
    const iconWrapperId = type === 'income' ? 'incomeIconWrapper' : 'expenseIconWrapper';

    const modal = document.getElementById(modalId);
    const iconWrapper = document.getElementById(iconWrapperId);

    if (!modal) return;

    if (open) {
        modal.classList.add('show');
    } else {
        modal.classList.remove('show');
        if (iconWrapper) iconWrapper.classList.remove('show')
    }


}


$(document).ready(function () {
    $('#incomeIconTrigger').on('click', function () {
        $('#incomeIconWrapper').toggleClass('show');
    });

    $('#incomeIconGrid .icon-option').on('click', function () {

        const iconText = $(this).text();
        $('#incomeIconTrigger').text(iconText);
        $('#incomeIconInput').val(iconText);
        $('#incomeIconWrapper').removeClass('show');

    });


    $('#expenseIconTrigger').on('click', function () {
        $('#expenseIconWrapper').toggleClass('show');
    });

    $('#expenseIconGrid .icon-option').on('click', function () {

        const iconText = $(this).text();
        $('#expenseIconTrigger').text(iconText);
        $('#expenseIconInput').val(iconText);
        $('#expenseIconWrapper').removeClass('show');

    });


    $("#save-account-change-button").click(function (e) {
        e.preventDefault();
        console.log("Clicked");
        const clientId = 'downstream-client';

        console.log(JSON.stringify(account))

        $.ajax({
            url: `/client-status?clientId=${encodeURIComponent(clientId)}`,
            type: 'GET',
            contentType: 'application/json',
            success: function (status) {
                if (status.connected) {
                    console.log(`Already connected to ${clientId}, saving directly...`);
                    saveData();
                } else {
                    console.log(`Not connected to ${clientId}, starting OAuth...`);
                    startOAuthFlow(clientId);
                }
            },

            error: function (xhr, status, error) {
                console.error("Status check failed:", error);
            }
        });
    });

    function saveData() {


        $.ajax({
            url: `/account`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(account),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(response) {
                console.log("Success:", response);
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
            }
        })


        // fetch('/account', {
        //     method: 'POST',
        //     headers: { 'Content-Type': 'application/json' },
        //     body: JSON.stringify(account)
        // })
        //     .then(response => response)
        //     .then(data => console.log("Saved successfully:", data))
        //     .catch(err => console.error("Save failed:", err));



    }
    function startOAuthFlow(clientId) {
        const authWindow = window.open(`/oauth2/authorization/${clientId}`, 'oauth2Login', 'width=100vh,height=100vh');

        const checkAuth = setInterval(() => {
            if (authWindow.closed) {
                clearInterval(checkAuth);
                saveData();
            }
        }, 1000);
    }


});



















































































(function () {
    const items = Array.from(document.querySelectorAll('.income-item'));
    let openItem = null;

    function closeOpen() {
        if (openItem) openItem.classList.remove('revealed');
        openItem = null;
    }

    items.forEach(item => {
        let startX = 0;
        let currentX = 0;
        let dragging = false;

        const body = item.querySelector('.income-body');

        // ----- Touch -----
        item.addEventListener('touchstart', (e) => {
            closeOpen();
            startX = e.touches[0].clientX;
            currentX = startX;
            dragging = true;
        }, {passive: true});

        item.addEventListener('touchmove', (e) => {
            if (!dragging) return;
            currentX = e.touches[0].clientX;
            const dx = currentX - startX;
            if (dx < 0) { // moving left
                body.style.transform = `translateX(${Math.max(dx, -140)}px)`;
            }
        }, {passive: true});

        item.addEventListener('touchend', () => {
            dragging = false;
            const dx = currentX - startX;
            body.style.transform = '';
            if (dx < -60) {
                item.classList.add('revealed');
                openItem = item;
            } else {
                item.classList.remove('revealed');
            }
        });

        // ----- Mouse (desktop drag) -----
        item.addEventListener('mousedown', (e) => {
            if (e.button !== 0) return;
            closeOpen();
            startX = e.clientX;
            currentX = startX;
            dragging = true;
            item.classList.add('dragging');
            e.preventDefault();
        });

        window.addEventListener('mousemove', (e) => {
            if (!dragging) return;
            currentX = e.clientX;
            const dx = currentX - startX;
            if (dx < 0) {
                body.style.transform = `translateX(${Math.max(dx, -140)}px)`;
            }
        });

        window.addEventListener('mouseup', () => {
            if (!dragging) return;
            dragging = false;
            const dx = currentX - startX;
            body.style.transform = '';
            item.classList.remove('dragging');
            if (dx < -60) {
                item.classList.add('revealed');
                openItem = item;
            } else {
                item.classList.remove('revealed');
            }
        });

        // ----- Keyboard accessibility -----
        item.addEventListener('keydown', (e) => {
            if (e.key === 'ArrowLeft') {
                item.classList.add('revealed');
                openItem = item;
            }
            if (e.key === 'ArrowRight' || e.key === 'Escape') {
                item.classList.remove('revealed');
                if (openItem === item) openItem = null;
            }
        });
    });

    // Close when clicking elsewhere
    document.addEventListener('click', (e) => {
        const isItem = e.target.closest('.income-item');
        if (!isItem) closeOpen();
    });
})();
