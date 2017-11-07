import ValidationService from "../ValidationService";
import TableActions from "../userlist/UserListPageActions";
import CreateEditModalFields from "./CreateEditModalFields";
import $ from "jquery";
import "bootstrap";

export default class CreateEditModal {

    constructor(strategy) {
        this.strategy = strategy;
        this.modalFields = new CreateEditModalFields();
    }

    openModal() {
        let thisFunc = this.sendRequest.bind(this);

        let errorOutput = document.getElementById("crModalErrorOutput");
        errorOutput.innerHTML = "";
        $("#generalModal").modal('show');
        $("#requestButton").off();

        let button = document.getElementById("requestButton");
        $(button).on("click", function () {
            thisFunc();
        });
        button.innerHTML = this.strategy.buttonName;

        document.getElementById("modalTitle").innerHTML = this.strategy.headTitle;

        this.strategy.prepareModal(this.modalFields);
    }

    async sendRequest() {
        let validationService = new ValidationService(
            [this.modalFields.userName, this.modalFields.firstName, this.modalFields.lastName],
            [this.modalFields.password, this.modalFields.confirmPassword],
            this.strategy.passwordRequired
        );

        if (validationService.validate()) {

            let isOk = await this.strategy.sendActionRequest(this.modalFields);
            console.log("isok" + isOk);
            if (isOk) {
                $("#successAlert").show();
                new TableActions().clearTable();
                new TableActions().generateTable();
                setTimeout(() => {
                    $("#successAlert").hide();
                }, 3000);
                $("#generalModal").modal('hide')
            }
        }
    }
}