import React from "react";
import { Link } from "react-router-dom";

function ProductSearchResult({ props }) {
  return (
    <div className="absolute bg-white z-10 w-max mx-auto border rounded-lg my-2 overflow-auto">
      <ul>
        {props.map((item, index) => (
          <li key={index}>
            <Link to={`/productDetail/${item.id}`} className="hover:opacity-70">
              <div className="flex justify-between items-center border-b gap-4 px-4 py-2 my-2">
                <img
                  src={item.image}
                  className="w-12 border rounded-full"
                  alt=""
                />
                <p className="font-mono">{item.name}</p>
              </div>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ProductSearchResult;
