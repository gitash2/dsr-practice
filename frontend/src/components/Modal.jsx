export default function Modal({ children, onClose }) {
    return (
        <div style={backdropStyle}>
            <div style={dialogStyle}>
                <div style={contentStyle}>
                    <button
                        type="button"
                        style={closeButtonStyle}
                        onClick={onClose}
                        aria-label="Close"
                    >
                        ×
                    </button>
                    {children}
                </div>
            </div>
        </div>
    );
}

const backdropStyle = {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100vw",
    height: "100vh",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    zIndex: 1050,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
};

const dialogStyle = {
    maxWidth: "500px",
    width: "100%",
};

const contentStyle = {
    backgroundColor: "#fff",
    borderRadius: "10px",
    padding: "2rem",
    position: "relative",
    boxShadow: "0 4px 20px rgba(0,0,0,0.2)",
    color: "#000",
};

const closeButtonStyle = {
    position: "absolute",
    top: "10px",
    right: "15px",
    background: "none",
    border: "none",
    fontSize: "1.5rem",
    cursor: "pointer",
    color: "#888",
};
