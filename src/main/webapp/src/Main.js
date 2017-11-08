import TableActions from "./userlist/UserListPageActions";
import "bootstrap";

window.onload = function () {
   let tableActions = new TableActions();
   tableActions.generateTable();
   $("#refreshButton").on("click", () => {
         tableActions.clearTable();
         tableActions.generateTable();
   });
};