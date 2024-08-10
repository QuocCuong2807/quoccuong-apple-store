import React, { useEffect, useState } from "react";
import axios from "axios";
import Modal from "../common/Modal";
import ManageProduct from "./ManageProduct";
import ProductForm from "../common/ProductForm";
import { handleLogout, showErrorDialog, showSuccessDialog } from "../../reuse";
import Loading from "../common/Loading";
import EditProductForm from "../common/EditProductForm";
import { useNavigate } from "react-router-dom";

function ProductManagement() {
  const [showProductForm, setShowProductForm] = useState(false);
  const [showEditProductForm, setShowEditProductForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const auth = JSON.parse(localStorage.getItem("auth"));
  const { accessToken } = auth;
  const navigate = useNavigate();
  console.log(accessToken);
  const handleShowProductForm = () => {
    setShowProductForm(true);
  };
  const handleHideProductForm = () => {
    setProduct(initProduct);
    setProductMainImage(undefined);
    setProductImagesDescription([]);
    setShowProductForm(false);
  };
  const handleShowEditProductForm = (item) => {
    setProduct({
      id: item.id,
      categoryId: item.category.id,
      name: item.name,
      price: item.price,
      technicalDetails: item.technicalDetails,
      color: item.color,
      description: item.description,
    });
    setShowEditProductForm(true);
  };
  const handleHideEditProductForm = () => {
    setProduct(initProduct);
    setProductMainImage(undefined);
    setProductImagesDescription([]);
    setShowEditProductForm(false);
  };

  const initCategoryFilter = "ALL";
  const initPriceFilter = "DESC";
  const [targetPage, setTargetPage] = useState(0);
  const [categoryFilter, setCategoryFilter] = useState(initCategoryFilter);
  const [priceFilter, setPriceFilter] = useState(initPriceFilter);
  const [pagination, setPagination] = useState({});

  const handlePageClick = (e) => {
    setTargetPage(e.selected);
  };

  const handleChangeCategoryFilter = (e) => {
    setCategoryFilter(e.target.value);
  };

  const handleChangePriceFilter = (e) => {
    setPriceFilter(e.target.value);
  };

  const initProduct = {
    id: 0,
    name: "",
    categoryId: 1,
    price: 0,
    technicalDetails: "",
    color: "",
    description: "",
  };
  const [product, setProduct] = useState(initProduct);
  const [productMainImage, setProductMainImage] = useState();
  const [productImageDescription, setProductImagesDescription] = useState([]);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    getAllCategories();
  }, []);

  useEffect(() => {
    getAllProducts(categoryFilter, priceFilter, targetPage);
  }, [categoryFilter, priceFilter, targetPage]);

  const getAllCategories = async () => {
    const url = "http://localhost:8080/api/v2/categories";
    try {
      const response = await axios.get(url);
      setCategories(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getAllProducts = async (categoryFilter, priceFilter, targetPage) => {
    const url = `http://localhost:8080/api/v1/products?category=${categoryFilter}&price=${priceFilter}&p=${targetPage}`;

    try {
      const response = await fetch(url);
      const responseJson = await response.json();
      const { data } = responseJson;

      setProducts(data);

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

  console.log(products);

  const handleChangeProductInfo = (e) => {
    setProduct((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleChangeMainImage = (e) => {
    setProductMainImage(e.target.files[0]);
  };

  const handleChangeImagesDescription = (e) => {
    setProductImagesDescription(e.target.files);
  };

  console.log(product);
  console.log(productMainImage);
  console.log(productImageDescription);

  const handleAddNewProduct = async () => {
    const form = new FormData();
    const url = "http://localhost:8080/api/product";

    setLoading(true);

    //append attr to form
    form.append("name", product.name);
    form.append("categoryId", product.categoryId);
    form.append("color", product.color);
    form.append("price", product.price);
    form.append("technicalDetails", product.technicalDetails);
    form.append("description", product.description);
    form.append("image", productMainImage);

    for (let i = 0; i < productImageDescription.length; i++) {
      form.append("imageList", productImageDescription[i]);
    }

    try {
      const response = await axios.post(url, form, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      handleHideProductForm();
      setTargetPage(0);
      setCategoryFilter(initCategoryFilter);
      setPriceFilter(initPriceFilter);
      getAllProducts(categoryFilter, priceFilter, targetPage);
      showSuccessDialog(response.data);
    } catch (error) {
      if (error.response.status === 401 || error.response.status === 403) {
        showErrorDialog("Phiên đăng nhập đã hết hạn");
        handleLogout(navigate);
      } else {
        showErrorDialog(error.response.data.message);
      }
    }
    handleHideProductForm();
    setLoading(false);
  };

  const handleEditProduct = async () => {
    const form = new FormData();
    const url = "http://localhost:8080/api/product";

    setLoading(true);

    //append attr to form
    form.append("id", product.id);
    form.append("name", product.name);
    form.append("categoryId", product.categoryId);
    form.append("color", product.color);
    form.append("price", product.price);
    form.append("technicalDetails", product.technicalDetails);
    form.append("description", product.description);
    form.append("image", productMainImage);

    for (let i = 0; i < productImageDescription.length; i++) {
      form.append("imageList", productImageDescription[i]);
    }

    try {
      const response = await axios.put(url, form, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      handleHideEditProductForm();
      setTargetPage(0);
      setCategoryFilter(initCategoryFilter);
      setPriceFilter(initPriceFilter);
      getAllProducts(categoryFilter, priceFilter, targetPage);
      showSuccessDialog(response.data);
    } catch (error) {
      if (error.response.status === 401 || error.response.status === 403) {
        showErrorDialog("Phiên đăng nhập đã hết hạn");
        handleLogout(navigate);
      } else {
        showErrorDialog(error.response.data.message);
      }
    }
    handleHideEditProductForm();
    setLoading(false);
  };

  return loading ? (
    <Loading />
  ) : (
    <div>
      <ManageProduct
        products={products}
        categories={categories}
        priceFilter={priceFilter}
        OnShowProductModal={handleShowProductForm}
        OnShowEditProductModal={handleShowEditProductForm}
        pagination={pagination}
        OnPageClick={handlePageClick}
        OnCategoryFilterChange={handleChangeCategoryFilter}
        OnPriceFilterChange={handleChangePriceFilter}
      />
      <Modal show={showProductForm} OnHideModal={handleHideProductForm}>
        <ProductForm
          product={product}
          category={categories}
          OnProductInfoChange={handleChangeProductInfo}
          OnMainImageChange={handleChangeMainImage}
          OnImageDescriptionChange={handleChangeImagesDescription}
          OnSubmitProduct={handleAddNewProduct}
        />
      </Modal>
      <Modal show={showEditProductForm} OnHideModal={handleHideEditProductForm}>
        <EditProductForm
          product={product}
          categories={categories}
          OnProductInfoChange={handleChangeProductInfo}
          OnMainImageChange={handleChangeMainImage}
          OnImageDescriptionChange={handleChangeImagesDescription}
          OnSubmitEditProduct={handleEditProduct}
        />
      </Modal>
    </div>
  );
}

export default ProductManagement;
