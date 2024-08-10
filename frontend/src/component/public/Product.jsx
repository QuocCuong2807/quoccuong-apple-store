import React, { useEffect } from "react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import IphoneFilter from "../common/IphoneFilter";
import IPadFilter from "../common/IPadFilter";
import MacFilter from "../common/MacFilter";
import ImageSlider from "../common/ImageSlider";
import Pagination from "../common/Pagination";
import ProductList from "./ProductList";

function Product() {
  const { category_id } = useParams();
  const [products, setProducts] = useState([]);
  const [pagination, setPagination] = useState({});
  const [targetPage, setTargetPage] = useState(0);
  const [productSeriesFilter, setProductSeriesFilter] = useState("ALL");
  const [priceFilter, setPriceFilter] = useState("ASC");

  const handlePageClick = (e) => {
    setTargetPage(e.selected);
  };

  const handleChangeProductSeries = (e) => {
    setProductSeriesFilter(e.target.name);
  };
  const handlePriceFilterChange = (e) => {
    setPriceFilter(e.target.value);
  };

  useEffect(() => {
    getAllProductsByCategoryId(
      productSeriesFilter,
      priceFilter,
      category_id,
      targetPage
    );
  }, [category_id, productSeriesFilter, priceFilter, targetPage]);

  console.log(targetPage);

  const getAllProductsByCategoryId = async (
    series,
    price,
    categoryId,
    targetPage
  ) => {
    const url = `http://localhost:8080/api/v2/products?series=${series}&price=${price}&category=${categoryId}&p=${targetPage}`;
    try {
      const response = await fetch(url);
      const responseJson = await response.json();
      const { data } = responseJson;
      console.log(responseJson);
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

  return (
    <div className="text-center py-10">
      <h1 className="font-bold font-mono text-3xl mb-10">iPhone</h1>
      <ImageSlider />
      {category_id === "1" ? (
        <IphoneFilter
          OnProductSeriesChange={handleChangeProductSeries}
          OnPriceFilterChange={handlePriceFilterChange}
        />
      ) : category_id === "2" ? (
        <IPadFilter
          OnProductSeriesChange={handleChangeProductSeries}
          OnPriceFilterChange={handlePriceFilterChange}
        />
      ) : category_id === "3" ? (
        <MacFilter
          OnProductSeriesChange={handleChangeProductSeries}
          OnPriceFilterChange={handlePriceFilterChange}
        />
      ) : (
        ""
      )}
      <ProductList
        props={products}
        pagination={pagination}
        OnPageClick={handlePageClick}
      />
      {/* <Pagination /> */}
    </div>
  );
}

export default Product;
