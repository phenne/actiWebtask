class DeleteModal {

    constructor(user) {
        this.user = user;
        this.errorField = document.getElementById("deleteModalOutputError");
    }

    openModal() {
        document.getElementById("deleteUsername").innerHTML = this.user.userName;
        $('#confirmDeleteButton').off();

        let bindFunc = this.sendActionRequest.bind(this);
        $("#confirmDeleteButton").on("click", function () {
            bindFunc();
        });

        $("#deleteModal").modal("show");
    }

    async sendActionRequest() {
        let result = await new DeleteUserRequest(this.user.id, this.errorField).send();

        if (result) {
            $("#successAlert").show();
            new TableActions().clearTable();
            new TableActions().generateTable();
            $("#deleteModal").modal('hide');
            setTimeout(() => {
                console.log("hidden");
                $("#successAlert").hide();
            }, 3000);
        }
    }
}