import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")
TRAIN_DIR = os.path.join(DATA_DIR, "train")
VAL_DIR = os.path.join(DATA_DIR, "val")
TEST_RESERVED_DIR = os.path.join(DATA_DIR, "test_reserved")

MODEL_SAVE_PATH = os.path.join(BASE_DIR, "models", "resnet50_bone_cancer.pth")

IMG_SIZE = 224
BATCH_SIZE = 16
EPOCHS = 10
LEARNING_RATE = 0.001
NUM_CLASSES = 2