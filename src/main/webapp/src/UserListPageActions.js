class TableActions {

    clearTable() {
        $("#userListTable tbody tr").remove();
        $("#createButton").remove();
    }

    //TODO Шаблоны и стратегии для таблицы
    async generateTable() {
        let currentUserRequest = await new CurrentUserRequest().send();
        let currentUser = currentUserRequest.result;
        document.getElementById("thisUsername").innerHTML = currentUser.userName;

        let requestResult = await new AllUsersRequest().send();
        let userList = requestResult.result.list;

        let element = document.getElementById("userListTable").getElementsByTagName('tbody')[0];

        for (let user of userList) {
            let currentRowsLength = element.rows.length;
            let row = element.insertRow(currentRowsLength);

            let firstName = row.insertCell(0);
            let lastName = row.insertCell(1);
            let username = row.insertCell(2);

            firstName.innerHTML = user.firstName;
            lastName.innerHTML = user.lastName;
            username.innerHTML = user.userName;

            if (currentUser.manager) {
                let buttonGroup = row.insertCell(3);
                let div = buttonGroup.appendChild(document.createElement("div"));
                $(div).addClass("btn-group");

                let editButton = div.appendChild(document.createElement("button"));
                $(editButton).addClass("btn");
                editButton.setAttribute("type", "button");
                editButton.innerHTML = "Edit";
                //TODO Сменить на айдишник
                $(editButton).on("click", () => {
                    new CreateEditModal(new EditUserStrategy(user)).openModal();
                });

                let deleteButton = div.appendChild(document.createElement("button"));
                $(deleteButton).addClass("btn");
                deleteButton.setAttribute("type", "button");
                deleteButton.innerHTML = "Delete";
                if (currentUser.userName === user.userName) {
                    deleteButton.disabled = true;
                } else {
                    //TODO Сменить на айдишник
                    $(deleteButton).on("click", () => {
                        new DeleteModal(user).openModal();
                    });
                }
            }
        }
        if (currentUser.manager) {
            let bottomButtons = document.getElementById("bottomButtons");

            let createButton = bottomButtons.appendChild(document.createElement("input"));
            createButton.setAttribute("type", "button");
            createButton.setAttribute("id", "createButton");
            $(createButton).addClass("btn");
            createButton.setAttribute("value", "Create");

            $(createButton).on("click", () => {
                new CreateEditModal(new CreateUserStrategy()).openModal();
            });
        }
    }
}