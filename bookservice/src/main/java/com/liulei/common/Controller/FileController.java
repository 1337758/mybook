package com.liulei.common.Controller;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.liulei.common.util.ConstantUtils;
import com.liulei.common.vo.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.READ;

/**
 * @description
 * @auther runze
 * @date 2019/7/29 10:27
 */

@Api
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    /**
     * @description: 文件上传接口
     * @Author: runze
     * @Date: 2019/7/29 10:31
     */
    @RequestMapping("fileUpload")
    @ApiOperation(value = "图片上传接口", notes = "")
    public ResultResponse<String> fileUpload(@RequestParam("fileData") MultipartFile file) {
        ResultResponse<String> resultResponse = new ResultResponse<>();
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            originalFilename = System.currentTimeMillis() + originalFilename;
            int size = (int) file.getSize();
            // 文件根路径
            String path = ConstantUtils.FILE_PATH + "/file";
            File dest = new File(path + "/" + originalFilename);
            log.info("上传文件：{}---路径：{}--->大小：{}", originalFilename, path, size);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            try {
                // 保存文件
                file.transferTo(dest);
                resultResponse.setData(path + "/" + originalFilename);
            } catch (IllegalStateException e) {
                resultResponse.error("保存文件报错");
                log.error("保存文件报错", e);
                e.printStackTrace();
            } catch (IOException e) {
                resultResponse.error("保存文件报错");
                log.error("保存文件报错", e);
                e.printStackTrace();
            }
        }
        return resultResponse;
    }


    /**
     * @description: 显示原文件（图片）
     * @Author: runze
     * @Date: 2019/7/29 10:47
     */
    @ApiOperation(value = "显示原文件（图片）", notes = "")
    @ApiImplicitParam(name = "path", value = "服务器图片路径", required = true, dataType = "String")
    @GetMapping(value = "/getImg")
    public void getImg(String path, HttpServletResponse response) {
        if(StringUtils.isNotBlank(path)) {
            // 输入输出流
            FileInputStream fis = null;
            OutputStream os = null;
            // 组织完整的文件路径
            path = new StringBuilder(ConstantUtils.FILE_PATH).append(path).toString();
            try {
                fis = new FileInputStream(path);
                os = response.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
            } catch (Exception e) {
                log.error("显示原文件（图片）报错", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * @description: 显示图片的缩略图 使用thumbnailator插件
     * @Author: runze
     * @Date: 2019/7/29 10:50
     */
    @ApiOperation(value = "显示图片的缩略图，根据传入宽高拉伸", notes = "")
    @ApiImplicitParams({ @ApiImplicitParam(name = "path", value = "服务器图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "wh", value = "想要获取图片的宽高 格式：100-100", required = false, dataType = "String") })
    @GetMapping(value = "/thumbImg")
    public void thumbImg(String path, String wh, HttpServletResponse response) {
        if(StringUtils.isNotBlank(path)) {
            // 缩略图
            BufferedImage thumbnail = null;
            // 需要压缩的尺寸，默认
            Integer width = 100;
            Integer height = 100;
            // 组织完整的文件路径
            path = new StringBuilder(ConstantUtils.FILE_PATH).append(path).toString();
            try {

                // 获取原尺寸的图片
                BufferedImage image = ImageIO.read(new FileInputStream(path));
                // 读取原始图片的宽高
                Integer widthOrig = image.getWidth();
                Integer heightOrig = image.getHeight();

                // 判断是否传入了需要压缩的尺寸
                if (wh == null || // 没有传入压缩尺寸
                        (StringUtils.isNotBlank(wh) && wh.indexOf("-") > -1)) { // 传入了完整的需要压缩的宽高，eg：100-100

                    // 传入了宽高则使用出入的宽高
                    if (wh != null) {
                        // 需要压缩的宽度
                        width = Integer.parseInt(wh.split("-")[0]);
                        // 需要压缩的高度
                        height = Integer.parseInt(wh.split("-")[1]);
                    }

                    // 图片 宽高比例
                    Float ratioOrig = (float) widthOrig / heightOrig;
                    Float ratio = (float) width / height;

                    // 根据尺寸进行压缩，若原图片比例与压缩比例不一致，不损坏原图的情况下，则将相应的缩小尺寸
                    if (ratioOrig == ratio) {
                        // 获取缩略图
                        thumbnail = Thumbnails.of(image).forceSize(width, height).asBufferedImage();
                    } else {
                        if (ratioOrig > ratio) {
                            thumbnail = Thumbnails.of(image).width(width).asBufferedImage();
                        } else {
                            thumbnail = Thumbnails.of(image).height(height).asBufferedImage();
                        }
                        // 以中心按压缩比例进行裁剪，这样则一定会得到要求尺寸的图片，但是会损坏原有的图片
                        // thumbnail =
                        // Thumbnails.of(image).sourceRegion(Positions.CENTER,
                        // width, height).size(width,height).asBufferedImage();
                    }

                } else { // 只传入了高度-方便页面设置高度

                    height = Integer.parseInt(wh);
                    // 只传入高度时，宽度根据高度压缩比例得到
                    width = widthOrig * height / heightOrig;
                    thumbnail = Thumbnails.of(image).forceSize(width, height).asBufferedImage();

                }

                // 输出流
                OutputStream os = response.getOutputStream();
                // 将缩略图写入输入流，JPEG格式
                ImageIO.write(thumbnail, "png", os);
                os.flush();

            } catch (IOException e) {
                log.error("显示图片的缩略图 使用thumbnailator插件报错", e);
                e.printStackTrace();
            }
        }
    }


    @ApiOperation(value = "显示图片的缩略图，根据传入宽高裁剪", notes = "")
    @ApiImplicitParams({ @ApiImplicitParam(name = "path", value = "服务器图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "wh", value = "想要获取图片的宽高 格式：100-100", required = false, dataType = "String") })
    @GetMapping(value = "/cutImg")
    public void cutImg(String path, String wh, HttpServletResponse response) {
        if(StringUtils.isNotBlank(path)) {
            // 缩略图
            BufferedImage thumbnail = null;
            // 需要压缩的尺寸，默认20*20
            Integer width = 100;
            Integer height = 100;
            // 组织完整的文件路径
            path = new StringBuilder(ConstantUtils.FILE_PATH).append(path).toString();
            try {

                // 获取原尺寸的图片
                BufferedImage image = ImageIO.read(new FileInputStream(path));
                // 读取原始图片的宽高
                Integer widthOrig = image.getWidth();
                Integer heightOrig = image.getHeight();

                // 判断是否传入了需要压缩的尺寸
                if (wh == null || // 没有传入压缩尺寸
                        (StringUtils.isNotBlank(wh) && wh.indexOf("-") > -1)) { // 传入了完整的需要压缩的宽高，eg：100-100

                    // 传入了宽高则使用出入的宽高
                    if (wh != null) {
                        // 需要压缩的宽度
                        width = Integer.parseInt(wh.split("-")[0]);
                        // 需要压缩的高度
                        height = Integer.parseInt(wh.split("-")[1]);
                    }

                    // 图片 宽高比例
                    Float ratioOrig = (float) widthOrig / heightOrig;
                    Float ratio = (float) width / height;

                    // 根据尺寸进行压缩，若原图片比例与压缩比例不一致，不损坏原图的情况下，则将相应的缩小尺寸
                    if (Math.abs(ratioOrig - ratio) <= 0) {
                        // 获取缩略图
                        thumbnail = Thumbnails.of(image).forceSize(width, height).asBufferedImage();
                    } else {
                        if (ratioOrig > ratio) {
                            thumbnail = Thumbnails.of(image).width(width).asBufferedImage();
                        } else {
                            thumbnail = Thumbnails.of(image).height(height).asBufferedImage();
                        }
                        // 以中心按压缩比例进行裁剪，这样则一定会得到要求尺寸的图片，但是会损坏原有的图片
                        thumbnail = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height)
                                .asBufferedImage();
                    }

                } else { // 只传入了高度-方便页面设置高度

                    height = Integer.parseInt(wh);
                    // 只传入高度时，宽度根据高度压缩比例得到
                    width = widthOrig * height / heightOrig;
                    thumbnail = Thumbnails.of(image).forceSize(width, height).asBufferedImage();

                }

                // 输出流
                OutputStream os = response.getOutputStream();
                // 将缩略图写入输入流，JPEG格式
                ImageIO.write(thumbnail, "png", os);
                os.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @ApiOperation(value = "获取视频流", notes = "")
    @ApiImplicitParam(name = "path", value = "服务器视频路径", required = true, dataType = "String")
    @GetMapping("/getVideo")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, @RequestParam String path) {
        if(StringUtils.isNotBlank(path)) {
            final int buffLength = 1024 * 16;
            final long expireTime = 1000 * 60 * 60 * 24;
            final Pattern rangePattern = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");

            try {
                String videoFilename = URLDecoder.decode(path, "UTF-8");
                String videoPath = new StringBuilder(ConstantUtils.FILE_PATH).toString();
                Path video = Paths.get(videoPath, videoFilename);

                int length = (int) Files.size(video);
                int start = 0;
                int end = length - 1;

                String range = request.getHeader("Range");
                range = range == null ? "" : range;
                Matcher matcher = rangePattern.matcher(range);

                if (matcher.matches()) {
                    String startGroup = matcher.group("start");
                    start = startGroup.isEmpty() ? start : Integer.valueOf(startGroup);
                    start = start < 0 ? 0 : start;

                    String endGroup = matcher.group("end");
                    end = endGroup.isEmpty() ? end : Integer.valueOf(endGroup);
                    end = end > length - 1 ? length - 1 : end;
                }

                int contentLength = end - start + 1;

                // 断点续传设置
                response.reset();
                response.setBufferSize(buffLength);
                response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", videoFilename));
                response.setHeader("Accept-Ranges", "bytes");
                response.setDateHeader("Last-Modified", Files.getLastModifiedTime(video).toMillis());
                response.setDateHeader("Expires", System.currentTimeMillis() + expireTime);
                response.setContentType(Files.probeContentType(video));
                response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
                response.setHeader("Content-Length", String.format("%s", contentLength));
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                int bytesRead;
                int bytesLeft = contentLength;
                ByteBuffer buffer = ByteBuffer.allocate(buffLength);

                try (SeekableByteChannel input = Files.newByteChannel(video, READ);
                     OutputStream output = response.getOutputStream()) {

                    input.position(start);

                    while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
                        buffer.clear();
                        output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
                        bytesLeft -= bytesRead;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
