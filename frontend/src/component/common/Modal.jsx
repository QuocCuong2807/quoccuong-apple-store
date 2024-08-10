import React from "react";

function Modal({ show, OnHideModal, children }) {
  return show ? (
    <div onClick={OnHideModal}
      className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm 
        flex justify-center items-center z-10 overflow-auto"
    >
      <div className="overflow-auto" onClick={e => e.stopPropagation()}>
        {children}
      </div>
    </div>
  ) : (
    <></>
  );
}

export default Modal;
