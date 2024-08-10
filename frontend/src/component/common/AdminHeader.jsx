import React from "react";
import { CiHome } from "react-icons/ci";
import { AiOutlineProduct } from "react-icons/ai";
import { IoBarChartOutline } from "react-icons/io5";
import { MdLogout } from "react-icons/md";
import { RiProductHuntLine } from "react-icons/ri";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { showSuccessDialog } from "../../reuse";
import {handleLogout} from '../../reuse'
function AdminHeader({OnLogout}) {
  const navigate = useNavigate();
  

  const logout = (navigate) => {
    OnLogout()
    handleLogout(navigate)
  }
  
  return (
    <div
      id="docs-sidebar"
      className="w-64 h-full bg-gray-50 border-e border-gray-300 pt-7 pb-10 overflow-y-auto dark:bg-neutral-800 dark:border-neutral-700 "
    >
      <div className="px-6">
        <a
          className="flex-none text-xl font-semibold dark:text-white"
          aria-label="Brand"
        >
          fantastic_cuonnguyen
        </a>
      </div>
      <nav
        className="hs-accordion-group p-6 w-full flex flex-col flex-wrap"
        data-hs-accordion-always-open
      >
        <ul className="space-y-3.5">
          <li>
            <Link
              className="flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:bg-neutral-700 dark:text-white"
              to="/admin"
            >
              <CiHome />
              Dashboard
            </Link>
          </li>

          <li className="hs-accordion" id="users-accordion">
            <Link
              to="/admin/category"
              className="hs-accordion-toggle hs-accordion-active:text-blue-600 hs-accordion-active:hover:bg-transparent w-full text-start flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:bg-neutral-800 dark:hover:bg-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300 dark:hs-accordion-active:text-white"
            >
              <AiOutlineProduct />
              Category
            </Link>
          </li>
          <li className="hs-accordion" id="users-accordion">
            <Link
              to="/admin/product"
              className="hs-accordion-toggle hs-accordion-active:text-blue-600 hs-accordion-active:hover:bg-transparent w-full text-start flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:bg-neutral-800 dark:hover:bg-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300 dark:hs-accordion-active:text-white"
            >
              <RiProductHuntLine />
              Product
            </Link>
          </li>

          <li className="hs-accordion" id="projects-accordion">
            <Link
              to="/admin/transaction"
              className="hs-accordion-toggle hs-accordion-active:text-blue-600 hs-accordion-active:hover:bg-transparent w-full text-start flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:bg-neutral-800 dark:hover:bg-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300 dark:hs-accordion-active:text-white"
            >
              <svg
                className="size-4"
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <path d="M15.5 2H8.6c-.4 0-.8.2-1.1.5-.3.3-.5.7-.5 1.1v12.8c0 .4.2.8.5 1.1.3.3.7.5 1.1.5h9.8c.4 0 .8-.2 1.1-.5.3-.3.5-.7.5-1.1V6.5L15.5 2z" />
                <path d="M3 7.6v12.8c0 .4.2.8.5 1.1.3.3.7.5 1.1.5h9.8" />
                <path d="M15 2v5h5" />
              </svg>
              Transactions
            </Link>
          </li>

          {/* <li>
            <a
              className="flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:hover:bg-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300"
              href="#"
            >
              <IoBarChartOutline />
              Incomes
            </a>
          </li> */}
          <li>
            <a
              className="flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-100 dark:hover:bg-neutral-700 dark:text-neutral-400 dark:hover:text-neutral-300 hover:cursor-pointer"
              onClick={() => logout(navigate)}
            >
              <MdLogout />
              Log out
            </a>
          </li>
        </ul>
      </nav>
    </div>
  );
}

export default AdminHeader;
