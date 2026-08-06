import shutil
import os
from config import TEST_RESERVED_DIR

def add_new_test_image(image_path, label):
    """
    label: 'cancer' o 'normal'
    """
    dest_dir = os.path.join(TEST_RESERVED_DIR, label)
    os.makedirs(dest_dir, exist_ok=True)
    filename = os.path.basename(image_path)
    dest_path = os.path.join(dest_dir, filename)
    shutil.copy(image_path, dest_path)
    print(f"✅ Imagen agregada a datos reservados: {dest_path}")

if __name__ == "__main__":
    import sys
    if len(sys.argv) == 3:
        add_new_test_image(sys.argv[1], sys.argv[2])
    else:
        print("Uso: python add_data.py <ruta_imagen> <cancer|normal>")