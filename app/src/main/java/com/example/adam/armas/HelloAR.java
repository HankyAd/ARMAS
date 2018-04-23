//================================================================================================================================
//
//  Copyright (c) 2015-2018 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.adam.armas;

import android.database.Cursor;
import android.opengl.GLES20;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import cn.easyar.CameraCalibration;
import cn.easyar.CameraDevice;
import cn.easyar.CameraDeviceFocusMode;
import cn.easyar.CameraDeviceType;
import cn.easyar.CameraFrameStreamer;
import cn.easyar.Frame;
import cn.easyar.FunctorOfVoidFromPointerOfTargetAndBool;
import cn.easyar.ImageTarget;
import cn.easyar.ImageTracker;
import cn.easyar.QRCodeScanner;
import cn.easyar.Renderer;
import cn.easyar.StorageType;
import cn.easyar.Target;
import cn.easyar.TargetInstance;
import cn.easyar.TargetStatus;
import cn.easyar.Vec2I;
import cn.easyar.Vec4I;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static cn.easyar.engine.EasyAR.getApplicationContext;

public class HelloAR
{
    private CameraDevice camera;
    private CameraFrameStreamer streamer;
    private ArrayList<ImageTracker> trackers;
    private Renderer videobg_renderer;
    private ArrayList<VideoRenderer> video_renderers;
    private VideoRenderer current_video_renderer;
    private QRCodeScanner qrcode_scanner;
    private int tracked_target = 0;
    private int active_target = 0;
    private ARVideo video = null;
    private boolean viewport_changed = false;
    private Vec2I view_size = new Vec2I(0, 0);
    private int rotation = 0;
    private Vec4I viewport = new Vec4I(0, 0, 1280, 720);
    private int previous_qrcode_index = -1;
    private MessageAlerter onAlert;
    private MainActivity mainActivity;
    private DAO dao;

    public interface MessageAlerter
    {
        void invoke(String s);
    }

    public HelloAR(MainActivity mA)
    {
        trackers = new ArrayList<ImageTracker>();
        mainActivity = mA;
        dao = mainActivity.getDAO();
    }

    private void loadFromImage(ImageTracker tracker, String path)
    {
        ImageTarget target = new ImageTarget();
        String jstr = "{\n"
                + "  \"images\" :\n"
                + "  [\n"
                + "    {\n"
                + "      \"image\" : \"" + path + "\",\n"
                + "      \"name\" : \"" + path.substring(0, path.indexOf(".")) + "\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        target.setup(jstr, StorageType.Assets | StorageType.Json, "");
        tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target target, boolean status) {
                Log.i("HelloAR", String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
            }
        });
    }

    private void loadAllFromJsonFile(ImageTracker tracker, String path)
    {
        for (ImageTarget target : ImageTarget.setupAll(path, StorageType.Assets)) {
            tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
                @Override
                public void invoke(Target target, boolean status) {
                    try {
                        Log.i("HelloAR", String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
                    } catch (Throwable ex) {
                    }
                }
            });
        }
    }
    private void loadFromJsonFile(ImageTracker tracker, String path, String targetname)
    {
        ImageTarget target = new ImageTarget();
        target.setup(path, StorageType.Assets, targetname);
        tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target target, boolean status) {
                Log.i("HelloAR", String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
            }
        });
    }
    public boolean initialize(MessageAlerter onAlert)
    {
        camera = new CameraDevice();
        streamer = new CameraFrameStreamer();
        streamer.attachCamera(camera);
        qrcode_scanner = new QRCodeScanner();
        qrcode_scanner.attachStreamer(streamer);
        this.onAlert = onAlert;

        boolean status = true;
        status &= camera.open(CameraDeviceType.Default);
        camera.setSize(new Vec2I(1280, 720));

        if (!status) { return status; }
        ImageTracker tracker1 = new ImageTracker();
        ImageTracker tracker2 = new ImageTracker();
        tracker1.attachStreamer(streamer);
        tracker2.attachStreamer(streamer);
        tracker1.setSimultaneousNum(1);
        tracker2.setSimultaneousNum(2);
        loadFromJsonFile(tracker2, "targets.json", "argame");
        loadFromJsonFile(tracker1, "targets.json", "idback");
        loadAllFromJsonFile(tracker2, "targets2.json");
        loadAllFromJsonFile(tracker1, "targets3.json");
        loadFromImage(tracker1, "namecard.jpg");
        trackers.add(tracker1);
        trackers.add(tracker2);

        return status;
    }

    public void dispose()
    {
        for (ImageTracker tracker : trackers) {
            tracker.dispose();
        }
        trackers.clear();

        if (videobg_renderer != null) {
            videobg_renderer.dispose();
            videobg_renderer = null;
        }
        if (qrcode_scanner != null) {
            qrcode_scanner.dispose();
            qrcode_scanner = null;
        }
        if (streamer != null) {
            streamer.dispose();
            streamer = null;
        }
        if (camera != null) {
            camera.dispose();
            camera = null;
        }
    }
    public boolean start()
    {
        boolean status = true;
        status &= (camera != null) && camera.start();
        status &= (streamer != null) && streamer.start();
        status &= (qrcode_scanner != null) && qrcode_scanner.start();
        camera.setFocusMode(CameraDeviceFocusMode.Continousauto);
        for (ImageTracker tracker : trackers) {
            status &= tracker.start();
        }
        return status;
    }

    public boolean stop()
    {
        boolean status = true;
        for (ImageTracker tracker : trackers) {
            status &= tracker.stop();
        }
        status &= (qrcode_scanner != null) && qrcode_scanner.stop();
        status &= (streamer != null) && streamer.stop();
        status &= (camera != null) && camera.stop();
        return status;
    }

    public void initGL()
    {
        if (active_target != 0) {
            video.onLost();
            video.dispose();
            video  = null;
            tracked_target = 0;
            active_target = 0;
        }
        if (videobg_renderer != null) {
            videobg_renderer.dispose();
        }
        videobg_renderer = new Renderer();
        video_renderers = new ArrayList<VideoRenderer>();
        for (int k = 0; k < 3; k += 1) {
            VideoRenderer video_renderer = new VideoRenderer();
            video_renderer.init();
            video_renderers.add(video_renderer);
        }
        current_video_renderer = null;
    }

    public void resizeGL(int width, int height)
    {
        view_size = new Vec2I(width, height);
        viewport_changed = true;
    }

    private void updateViewport()
    {
        CameraCalibration calib = camera != null ? camera.cameraCalibration() : null;
        int rotation = calib != null ? calib.rotation() : 0;
        if (rotation != this.rotation) {
            this.rotation = rotation;
            viewport_changed = true;
        }
        if (viewport_changed) {
            Vec2I size = new Vec2I(1, 1);
            if ((camera != null) && camera.isOpened()) {
                size = camera.size();
            }
            if (rotation == 90 || rotation == 270) {
                size = new Vec2I(size.data[1], size.data[0]);
            }
            float scaleRatio = Math.max((float) view_size.data[0] / (float) size.data[0], (float) view_size.data[1] / (float) size.data[1]);
            Vec2I viewport_size = new Vec2I(Math.round(size.data[0] * scaleRatio), Math.round(size.data[1] * scaleRatio));
            viewport = new Vec4I((view_size.data[0] - viewport_size.data[0]) / 2, (view_size.data[1] - viewport_size.data[1]) / 2, viewport_size.data[0], viewport_size.data[1]);

            if ((camera != null) && camera.isOpened())
                viewport_changed = false;
        }
    }

    public void render()
    {
        GLES20.glClearColor(1.f, 1.f, 1.f, 1.f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (videobg_renderer != null) {
            Vec4I default_viewport = new Vec4I(0, 0, view_size.data[0], view_size.data[1]);
            GLES20.glViewport(default_viewport.data[0], default_viewport.data[1], default_viewport.data[2], default_viewport.data[3]);
            if (videobg_renderer.renderErrorMessage(default_viewport)) {
                return;
            }
        }

        if (streamer == null) { return; }
        Frame frame = streamer.peek();
        try {
            updateViewport();
            GLES20.glViewport(viewport.data[0], viewport.data[1], viewport.data[2], viewport.data[3]);

            if (videobg_renderer != null) {
                videobg_renderer.render(frame, viewport);
            }

            ArrayList<TargetInstance> targetInstances = frame.targetInstances();
            if (targetInstances.size() > 0) {
                TargetInstance targetInstance = targetInstances.get(0);
                Target target = targetInstance.target();
                int status = targetInstance.status();
                if (status == TargetStatus.Tracked) {
                    int id = target.runtimeID();

                    mainActivity.setAsbestosTrue();

                    if (active_target != 0 && active_target != id) {
                        video.onLost();
                        video.dispose();
                        video  = null;
                        tracked_target = 0;
                        active_target = 0;
                    }
                    if (tracked_target == 0) {
                        if (video == null && video_renderers.size() > 0) {
                            String target_name = target.name();
                            if  (video_renderers.get(1).texId() != 0) {
                                video = new ARVideo();
                                video.openTransparentVideoFile("gg.mp4", video_renderers.get(1).texId());
                                current_video_renderer = video_renderers.get(1);
                                mainActivity.setImageName(target_name);
                            }

                        }

                        if (video != null) {
                            video.onFound();
                            tracked_target = id;
                            active_target = id;
                        }
                    }
                    ImageTarget imagetarget = target instanceof ImageTarget ? (ImageTarget)(target) : null;
                    if (imagetarget != null) {
                        if (current_video_renderer != null) {
                            video.update();
                            if (video.isRenderTextureAvailable()) {
                                current_video_renderer.render(camera.projectionGL(0.2f, 500.f), targetInstance.poseGL(), imagetarget.size());
                            }
                        }
                    }
                }
            } else {
                if (tracked_target != 0) {
                    video.onLost();
                    tracked_target = 0;
                    mainActivity.setAsbestosFalse();
                }
            }
            if (frame.index() != previous_qrcode_index) {
                previous_qrcode_index = frame.index();
                String text = frame.text();
                //regex code
                String regex = "[0-9]+";
                if (text != null && !text.equals("") && text.matches(regex)) {
                    Cursor m = dao.getRoomByID(text);
                    if(m != null) {
                        Cursor n = dao.getAsbestosByRoomID(text);
                        n.moveToFirst();
                        System.out.println("AFAFAFAFAFAFAF");
                        updateJSON(n);
                        Log.i("HelloAR", "got qrcode: " + text);
                        onAlert.invoke("got qrcode: " + text);
                    }
                }
            }
        }
        finally {
            frame.dispose();
        }
    }

    public void updateJSON(Cursor mCursor){
        int i = 1;
        int count = mCursor.getCount();
        img[] im = new img[count];
        System.out.println("SAIFSAKDJBSAPDKJSH " + count);
        JSONObject image = new JSONObject();
        do{
            System.out.println("ssdksjdksjdksjdksjdksjdksjdksjdksjdks  "+mCursor.getString(2));
            try {
                image.put("image", "demo" + mCursor.getString(2));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                System.out.println("IMAGE PUT ERROR");
            }
            i++;
            mCursor.moveToNext();
        }while(!mCursor.isAfterLast());

        System.out.println(image.toString());

        File mFolder = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath().toString()+ "/armas");
        File jsonfile = new File(mFolder.getAbsolutePath() + "/targets.json");

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(jsonfile.getAbsolutePath()), "utf-8"));
            writer.write(image.toString());
            System.out.println("WRITE TO FILE");
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {System.out.println("DID NOT WRITE TO FILE");}
        }



        System.out.println(jsonfile.toString());
        if (!mFolder.exists()) {
            System.out.println("DIRECORY MADE");
            mFolder.mkdir();
        }
        try{

            jsonfile.createNewFile();
            System.out.println("FILE CREATED");
        } catch (IOException ae){
            System.out.println("FILE NOT CREATED" + ae);
        }

    }
}

class img{
    public String image;
    public String name;

    public void setImage(String img){
        image = img;
    }

    public void setName(String nm){
        name = nm;
    }
}