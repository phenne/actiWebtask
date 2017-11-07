//TODO: загрузка бутстрапа, jquery в глобальную видимость.
//TODO: чекнуть тесты.
//TODO: перевести на темплейты.
import TableActions from "./userlist/UserListPageActions";

window.onload = function () {
   new TableActions().generateTable();
};