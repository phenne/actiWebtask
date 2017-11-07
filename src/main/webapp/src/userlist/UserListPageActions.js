class TableActions {

    clearTable() {
        $("#userListTable tbody tr").remove();
        $("#createButton").remove();
    }

    async generateTable() {
        let currentUserRequest = await new CurrentUserRequest().send();
        this.currentUser = currentUserRequest.result;

        $("#thisUsername")[0].innerHTML = this.currentUser.userName;

        let allUsersRequest = await new AllUsersRequest().send();
        this.userList = allUsersRequest.result.list;

        let element = $("#userListTable tbody")[0];

        for (let user of this.userList) {
            let currentRowsIndex = element.rows.length;
            let row = element.insertRow(currentRowsIndex);

            row.insertCell(0).innerHTML = user.firstName;
            row.insertCell(1).innerHTML = user.lastName;
            row.insertCell(2).innerHTML = user.userName;
        }

        if (this.currentUser.manager) {
            this.addManagerElementsToTable();
            this.addCreateButton();
        }
    }

    addManagerElementsToTable() {

        let tableRows = $("#userListTable tbody")[0].rows;

        for (let i = 0; i < tableRows.length; i++) {
            let buttonGroup = tableRows[i].insertCell(3);
            let bgDiv = $(buttonGroup).append($("<div class='btn-group'>"));

            let editButton = $("<button class='btn' type='button'>Edit</button>");
            let deleteButton = $("<button class='btn' type='button'>Delete</button>")

            bgDiv.append(editButton);
            bgDiv.append(deleteButton);

            editButton.on("click", () => {
                new CreateEditModal(new EditUserStrategy(this.userList[i])).openModal();
            });

            if (this.currentUser.userName === this.userList[i].userName) {
                deleteButton.prop("disabled", true);
            } else {
                deleteButton.on("click", () => {
                    new DeleteModal(this.userList[i]).openModal();
                })
            }

            $(buttonGroup).append("</div>");
        }
    }

    addCreateButton() {
        let createButton = $("<button type='button' class='btn' id='createButton'>Create</button>");

        $("#bottomButtons").append(createButton);
        $(createButton).on("click", () => {
            new CreateEditModal(new CreateUserStrategy()).openModal();
        });
    }
}