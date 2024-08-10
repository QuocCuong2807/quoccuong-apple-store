import AdminHeader from "../component/common/AdminHeader";
import { Outlet } from "react-router-dom";
import Forbidden from "../component/common/Forbidden";

function Admin({OnLogout}) {
  const lsAuth = localStorage.getItem("auth");
  const authObj = lsAuth ? JSON.parse(lsAuth) : {};
  const { role } = authObj;
  return role ? (
    role.includes("ADMIN") ? (
      <div className="grid grid-cols-12">
        <div className="col-span-2">
          <AdminHeader OnLogout = {OnLogout}/>
        </div>
        <div className="col-span-10">
          <Outlet />
        </div>
      </div>
    ) : (
      <Forbidden />
    )
  ) : (
    <Forbidden />
  );
}

export default Admin;
