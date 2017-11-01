class CreateEditModal {

    constructor(strategy) {
        this.strategy = strategy;
        this.modalFields = new CreateEditModalFields();
    }

    openModal() {
        let thisFunc = this.sendActionRequest.bind(this);

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

    async sendActionRequest() {
        let validationService = new ValidationService(
            [this.modalFields.userName, this.modalFields.firstName, this.modalFields.lastName],
            [this.modalFields.password, this.modalFields.confirmPassword],
            this.strategy.passwordRequired
        );

        if (validationService.validate()) {

            let isOk = await this.strategy.sendActionRequest(this.modalFields);
            console.log("isok" + isOk);
            if (isOk) {
                this.strategy.afterRequestAction();
                this._closeModal();
            }
        }
    }

    _closeModal() {
        $("#generalModal").modal('hide')
    }
}