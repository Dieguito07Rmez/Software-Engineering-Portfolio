import torch
from PIL import Image
from model import BoneCancerResNet50
from config import MODEL_SAVE_PATH
from utils import get_transform

def predict_image(image_path):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = BoneCancerResNet50(num_classes=2).to(device)
    model.load_state_dict(torch.load(MODEL_SAVE_PATH, map_location=device))
    model.eval()

    transform = get_transform()
    image = Image.open(image_path).convert("RGB")
    input_tensor = transform(image).unsqueeze(0).to(device)

    with torch.no_grad():
        output = model(input_tensor)
        _, predicted = torch.max(output, 1)
        prob = torch.softmax(output, dim=1)

    class_names = ["Normal", "Cáncer"]
    return class_names[predicted.item()], prob[0][predicted.item()].item()

if __name__ == "__main__":
    import sys
    if len(sys.argv) > 1:
        cls, conf = predict_image(sys.argv[1])
        print(f"Predicción: {cls} (confianza: {conf:.2f})")