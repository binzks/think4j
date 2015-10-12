package org.think4jframework.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by zhoubin on 15/10/8.
 */
public class ImageUtils {

//    /**
//     * 判断一个文件是否图片，用创建图片的方式来判断，如果失败也认为不是图片
//     *
//     * @param input 待判断的文件
//     * @return true是图片 false不是图片
//     */
//    public static boolean isImage(Object input) {
//        boolean flag = false;
//        try {
//            if (null != ImageIO.createImageInputStream(input)) {
//                flag = true;
//            }
//        } catch (Exception e) {
//            //如果发生异常也认为不是图片
//        }
//        return flag;
//    }
//
//    /***
//     * 实现图像的缩放，如果宽度或者高度有一个<=0则用另一个进行等比例缩放
//     *
//     * @param inputStream 原文件流
//     * @param targetW     目标宽度
//     * @param targetH     目标高度
//     * @return 压缩后的文件byte数字
//     * @throws Exception 异常
//     */
//    public static byte[] resize(InputStream inputStream, int targetW, int targetH) throws Exception {
//if (targetH <)
//
//        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
//        File destFile = new File("C:\\temp\\456.jpg");
//        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
//        // 可以正常实现bmp、png、gif转jpg
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        encoder.encode(image); // JPEG编码
//        out.close();
//
//
//        BufferedImage source = ImageIO.read(inputStream);
//        // targetW，targetH分别表示目标长和宽
//        int type = source.getType();
//        BufferedImage target = null;
//        double sx = (double) targetW / source.getWidth();
//        double sy = (double) targetH / source.getHeight();
//        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
//        // 则将下面的if else语句注释即可
//        if (sx < sy) {
//            sx = sy;
//            targetW = (int) (sx * source.getWidth());
//        } else {
//            sy = sx;
//            targetH = (int) (sy * source.getHeight());
//        }
//        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
//            ColorModel cm = source.getColorModel();
//            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
//            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
//            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
//        } else
//            target = new BufferedImage(targetW, targetH, type);
//        Graphics2D g = target.createGraphics();
//        // smoother than exlax:
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
//        g.dispose();
//        return ((DataBufferByte) target.getData().getDataBuffer()).getData();
//    }

}
