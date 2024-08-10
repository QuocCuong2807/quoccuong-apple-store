import React, { useEffect, useState } from "react";
import ManageCategory from "./ManageCategory";
import Modal from "../common/Modal";
import ProductCateForm from "../common/ProductCateForm";
import axios from "axios";
import { showErrorDialog, showSuccessDialog } from "../../reuse";
import Loading from "../common/Loading";
import EditCategoryForm from "../common/EditCategoryForm";
import DeleteConfirm from "../common/DeleteConfirm";
import { handleLogout } from "../../reuse";
import { useNavigate } from "react-router-dom";

function CategoryManagement() {
  const [showProductCateForm, setShowProductCateForm] = useState(false);
  const [showEditCateForm, setShowEditCateForm] = useState(false);
  const [showDeleteCateForm, setShowDeleteCateForm] = useState(false);
  const [loading, setLoading] = useState(false);

  const initCategory = { name: "", description: "" };
  const [category, setCategory] = useState(initCategory);
  const [categories, setCategories] = useState([]);
  const [pagination, setPagination] = useState({});
  const [targetPage, setTargetPage] = useState(0);
  const [queryString, setQueryString] = useState("");
  const [categoryId, setCategoryId] = useState(0);
  const auth = JSON.parse(localStorage.getItem("auth"));
  const { accessToken } = auth;

  const navigate = useNavigate();

  console.log(accessToken);

  useEffect(() => {
    getAllCategories(queryString, targetPage);
  }, [targetPage]);

  const hideCategoryModal = () => {
    setCategory(initCategory);
    setShowProductCateForm(false);
  };
  const hideEditCategoryModal = () => {
    setCategory(initCategory);
    setShowEditCateForm(false);
  };
  const hideDeleteCategoryModal = () => {
    setCategoryId(0);
    setShowDeleteCateForm(false);
  };
  const handlePageClick = (e) => {
    setTargetPage(e.selected);
  };

  const handleChangeCategory = (e) => {
    setCategory((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleShowEditModal = (item) => {
    setCategory(item);
    setShowEditCateForm(true);
  };

  const handleShowDeleteModal = (id) => {
    setCategoryId(id);
    setShowDeleteCateForm(true);
  };

  // interact with back-end
  const getAllCategories = async (queryString, targetPage) => {
    try {
      const requestUrl = `http://localhost:8080/api/v1/categories?name=${queryString}&p=${targetPage}`;
      const response = await fetch(requestUrl);
      const responseJson = await response.json();
      const { data } = responseJson;

      setCategories(data);
      setPagination({
        isFirstPage: responseJson.firstPage,
        isLastPage: responseJson.lastPage,
        pageNo: responseJson.pageNumber,
        pageSize: responseJson.pageSize,
        totalPage: responseJson.totalPage,
      });
    } catch (error) {
      console.log(error);
    }
  };

  const addNewCategory = async () => {
    setLoading(true);
    try {
      const response = await axios.post(
        "http://localhost:8080/api/category",
        category,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      showSuccessDialog(response.data);
      hideCategoryModal();
    } catch (error) {
      if (error.response.status === 401 || error.response.status === 403) {
        showErrorDialog("Phiên đăng nhập đã hết hạn");
        handleLogout(navigate);
      } else {
        showErrorDialog(error.response.data.message);
      }
    }
    setLoading(false);
    getAllCategories(targetPage);
  };

  const editExistedCategory = async () => {
    try {
      setLoading(true);
      const response = await axios.put(
        "http://localhost:8080/api/category",
        category,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      showSuccessDialog(response.data);
      hideEditCategoryModal();
      setTargetPage(0);
      getAllCategories(queryString, targetPage);
    } catch (error) {
      if (error.response.status === 401 || error.response.status === 403) {
        showErrorDialog("Phiên đăng nhập đã hết hạn");
        handleLogout(navigate);
      } else {
        showErrorDialog(error.response.data.message);
      }
    }
    setLoading(false);
  };

  const deleteCategory = async (id) => {
    try {
      setLoading(true);
      const response = await axios.delete(
        `http://localhost:8080/api/category/${id}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      setLoading(false);
      showSuccessDialog(response.data);
      hideDeleteCategoryModal();
      setTargetPage(0);
      getAllCategories(queryString, targetPage);
    } catch (error) {
      if (error.response.status === 401 || error.response.status === 403) {
        showErrorDialog("Phiên đăng nhập đã hết hạn");
        handleLogout(navigate);
      } else {
        showErrorDialog(error.response.data.message);
      }
    }
  };

  console.log(categoryId);

  return loading ? (
    <Loading />
  ) : (
    <div>
      <ManageCategory
        OnShowCategoryModal={() => setShowProductCateForm(true)}
        categories={categories}
        pagination={pagination}
        OnPageClick={handlePageClick}
        OnShowEditForm={handleShowEditModal}
        OnShowDeleteForm={handleShowDeleteModal}
        OnQueryStringChange={(e) => setQueryString(e.target.value)}
        OnSubmitSearch={() => getAllCategories(queryString, targetPage)}
      />

      <Modal show={showProductCateForm} OnHideModal={() => hideCategoryModal()}>
        <ProductCateForm
          props={category}
          OnCategoryChange={handleChangeCategory}
          SubmitNewCategory={addNewCategory}
        />
      </Modal>

      <Modal
        show={showEditCateForm}
        OnHideModal={() => hideEditCategoryModal()}
      >
        <EditCategoryForm
          props={category}
          OnCategoryChange={handleChangeCategory}
          SubmitEditCategory={editExistedCategory}
        />
      </Modal>

      <Modal show={showDeleteCateForm}>
        <DeleteConfirm
          props={categoryId}
          OnHideModal={hideDeleteCategoryModal}
          OnSubmitDelete={deleteCategory}
        />
      </Modal>
    </div>
  );
}

export default CategoryManagement;
