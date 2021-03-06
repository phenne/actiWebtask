import CreateEditModal from "../modal/CreateEditModal";
import DeleteModal from "../modal/DeleteModal";
import {AllUsersRequest, CurrentUserRequest} from "../RequestSender"
import CreateUserStrategy from "../modalstrategy/CreateUserStrategy";
import EditUserStrategy from "../modalstrategy/EditUserStrategy";

export default class TableActions {

    constructor(parent) {
        this.parent = parent;
    }

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
            let bgDiv = $("<div class='btn-group'></div>").appendTo($(buttonGroup));

            let currentEditButtonId = "editButton" + i;
            let currentDeleteButtonId = "deleteButton" + i;

            bgDiv.append($(`<button class="btn" type="button" id="${currentEditButtonId}">Edit</button>`));
            bgDiv.append($(`<button class="btn" type="button" id="${currentDeleteButtonId}">Delete</button>`));

            $(`#${currentEditButtonId}`).on("click", () =>
                new CreateEditModal(new EditUserStrategy(this.userList[i].id)).openModal()
            );

            if (this.currentUser.userName === this.userList[i].userName) {
                $(`#${currentDeleteButtonId}`).prop("disabled", true);
            } else {
                $(`#${currentDeleteButtonId}`).on("click", () => {
                    new DeleteModal(this.userList[i]).openModal();
                })
            }
        }
    }

    addCreateButton() {
        $("#bottomButtons").append($("<button type='button' class='btn' id='createButton'>Create</button>"));

        $("#createButton").on("click", () => {
            new CreateEditModal(new CreateUserStrategy()).openModal();
        });
    }
}