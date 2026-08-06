import torch.nn as nn
import torchvision.models as models

class BoneCancerResNet50(nn.Module):
    def __init__(self, num_classes=2):
        super(BoneCancerResNet50, self).__init__()
        self.model = models.resnet50(weights='IMAGENET1K_V1')
        num_features = self.model.fc.in_features
        self.model.fc = nn.Linear(num_features, num_classes)

    def forward(self, x):
        return self.model(x)