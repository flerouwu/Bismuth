package dev.flero.bismuth.commands.screenshot;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class ImageTransferable implements Transferable {
    private final Image image;

    public ImageTransferable(Image image) {
        this.image = image;
    }

    @NotNull
    @Override
    public Object getTransferData(DataFlavor flavour) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavour)) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavour);
        }
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavour) {
        return flavour == DataFlavor.imageFlavor;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }
}