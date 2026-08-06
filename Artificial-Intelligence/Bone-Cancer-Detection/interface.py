import tkinter as tk
from tkinter import filedialog, messagebox
from PIL import Image, ImageTk
import predict
import add_data

class BoneCancerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Detección de Cáncer de Médula Ósea - ResNet50")
        self.root.geometry("600x500")
        self.root.configure(bg="#f0f0f0")

        title = tk.Label(root, text="Detección de Cáncer Óseo", font=("Arial", 18, "bold"), bg="#f0f0f0")
        title.pack(pady=10)

        self.image_label = tk.Label(root, bg="#f0f0f0")
        self.image_label.pack(pady=10)

        btn_load = tk.Button(root, text="📁 Cargar imagen nueva para prueba", command=self.load_image,
                             font=("Arial", 12), bg="#4CAF50", fg="white", padx=10, pady=5)
        btn_load.pack(pady=5)

        btn_predict = tk.Button(root, text="🔍 Predecir (cáncer o normal)", command=self.predict,
                                font=("Arial", 12), bg="#2196F3", fg="white", padx=10, pady=5)
        btn_predict.pack(pady=5)

        self.result_label = tk.Label(root, text="Resultado: ", font=("Arial", 14), bg="#f0f0f0")
        self.result_label.pack(pady=10)

        # Atajos de teclado (accesibilidad)
        root.bind('<Control-o>', lambda e: self.load_image())
        root.bind('<Control-p>', lambda e: self.predict())

        self.current_image_path = None

    def load_image(self):
        path = filedialog.askopenfilename(filetypes=[("Image files", "*.jpg *.jpeg *.png")])
        if path:
            self.current_image_path = path
            img = Image.open(path)
            img = img.resize((300, 300))
            self.photo = ImageTk.PhotoImage(img)
            self.image_label.config(image=self.photo)
            self.result_label.config(text="✅ Imagen cargada. Presiona 'Predecir'.")

    def predict(self):
        if not self.current_image_path:
            messagebox.showwarning("Advertencia", "Primero carga una imagen.")
            return
        try:
            clase, confianza = predict.predict_image(self.current_image_path)
            self.result_label.config(text=f"📊 Resultado: {clase} (confianza: {confianza:.2f})")
            respuesta = messagebox.askyesno("Guardar resultado",
                                            f"¿Deseas agregar esta imagen a los datos reservados como '{clase.lower()}'?")
            if respuesta:
                add_data.add_new_test_image(self.current_image_path, clase.lower())
                messagebox.showinfo("Éxito", "Imagen guardada en datos de prueba reservados.")
        except Exception as e:
            messagebox.showerror("Error", f"No se pudo predecir: {e}")

if __name__ == "__main__":
    root = tk.Tk()
    app = BoneCancerApp(root)
    root.mainloop()