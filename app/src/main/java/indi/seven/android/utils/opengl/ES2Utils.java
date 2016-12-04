package indi.seven.android.utils.opengl;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import junit.framework.Assert;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * @author unknow. I found it from my colleague's project about face recognization.
 * @date 2016/11/7
 * @description
 */
public class ES2Utils {
    private final static String TAG = ES2Utils.class.getSimpleName();

    private boolean bFrontCamera;
    private int mCameraDegree;

    private IntBuffer mTextureYid = IntBuffer.allocate(1);
    private IntBuffer mTextureUid = IntBuffer.allocate(1);
    private IntBuffer mTextureVid = IntBuffer.allocate(1);

    private int mFormat;
    private int mWidth;
    private int mHeight;

    private GLShader GLShaderTriangle = new GLShader();
    private GLShader GLShaderTextureGRAY = new GLShader();
    private GLShader GLShaderTextureGRAY16 = new GLShader();
    private GLShader GLShaderTextureNV12 = new GLShader();
    private GLShader GLShaderTextureNV21 = new GLShader();
    private GLShader GLShaderTextureYUYV = new GLShader();
    private GLShader GLShaderTextureUYVY = new GLShader();
    private GLShader GLShaderTextureI440_I422_I444 = new GLShader();
    private GLShader GLShaderTextureRGBA = new GLShader();
    private GLShader GLShaderTextureRGBX = new GLShader();

    public final static int ASVL_PAF_GRAY = 0x701;
    public final static int ASVL_PAF_GRAY16 = 0x702;
    public final static int ASVL_PAF_NV12 = 0x801;
    public final static int ASVL_PAF_NV21 = 0x802;
    public final static int ASVL_PAF_YUYV = 0x501;
    public final static int ASVL_PAF_UYVY = 0x503;
    public final static int ASVL_PAF_I444 = 0x604;
    public final static int ASVL_PAF_I422H = 0x603;
    public final static int ASVL_PAF_I420 = 0x601;
    public final static int ASVL_PAF_RGB = 0x204;
    public final static int ASVL_PAF_RGBA = 0x305;
    public final static int ASVL_PAF_RGBX = 0x303;
    public final static int ASVL_PAF_MJPEG = 0xFFFF;

    private class GLShader {
        public int programid;
        public int vertexShader;
        public int fragmentShader;
    }

    public ES2Utils(boolean bMirror, int degree) {
        mFormat = 0;
        mWidth = 0;
        mHeight = 0;
        bFrontCamera = bMirror;
        mCameraDegree = degree;
    }

    public void drawTexture(ByteBuffer rawBuffer, int format, int width, int height) {

        if ((format != ASVL_PAF_GRAY) && (format != ASVL_PAF_GRAY16) && (format != ASVL_PAF_NV12) && (format != ASVL_PAF_NV21) && (format != ASVL_PAF_YUYV) && (format != ASVL_PAF_UYVY)
                && (format != ASVL_PAF_I444) && (format != ASVL_PAF_I422H) && (format != ASVL_PAF_I420) && (format != ASVL_PAF_RGB) && (format != ASVL_PAF_RGBA) && (format != ASVL_PAF_RGBX)) {
            Assert.assertTrue("drawTexture:format not support", false);
        }

        int programid = 0;
        if (format == ASVL_PAF_GRAY) {
            createProgram(GLShaderTextureGRAY, VertexShaderTexture, FragmentShaderTextureGRAY);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
        } else if (format == ASVL_PAF_GRAY16) {
            createProgram(GLShaderTextureGRAY16, VertexShaderTexture, FragmentShaderTextureGRAY16);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
        } else if (format == ASVL_PAF_NV12) {
            programid = createProgram(GLShaderTextureNV12, VertexShaderTexture, FragmentShaderTextureNV12);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureU"), 1);
        } else if (format == ASVL_PAF_NV21) {
            programid = createProgram(GLShaderTextureNV21, VertexShaderTexture, FragmentShaderTextureNV21);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureU"), 1);
        } else if (format == ASVL_PAF_YUYV) {
            programid = createProgram(GLShaderTextureYUYV, VertexShaderTexture, FragmentShaderTextureYUYV);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureU"), 1);
        } else if (format == ASVL_PAF_UYVY) {
            programid = createProgram(GLShaderTextureUYVY, VertexShaderTexture, FragmentShaderTextureUYVY);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureU"), 1);
        } else if ((format == ASVL_PAF_I444) || (format == ASVL_PAF_I422H) || (format == ASVL_PAF_I420)) {
            programid = createProgram(GLShaderTextureI440_I422_I444, VertexShaderTexture, FragmentShaderTextureI420_I422_I444);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureU"), 1);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureV"), 2);
        } else if ((format == ASVL_PAF_RGB) || (format == ASVL_PAF_RGBX)) {
            programid = createProgram(GLShaderTextureRGBX, VertexShaderTexture, FragmentShaderTextureRGBX);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
        } else {
            programid = createProgram(GLShaderTextureRGBA, VertexShaderTexture, FragmentShaderTextureRGBA);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(programid, "u_textureY"), 0);
        }

        float projMatrix[] = new float[16];
        if (bFrontCamera) {
            Matrix.frustumM(projMatrix, 0, 0.5f, -0.5f, -0.5f, 0.5f, 2.0f, 6.0f);
        } else {
            Matrix.frustumM(projMatrix, 0, -0.5f, 0.5f, -0.5f, 0.5f, 2.0f, 6.0f);
        }

        float mvMatrix[] = new float[16];
        Matrix.setLookAtM(mvMatrix, 0, 0.0f, 0.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mvMatrix, 0, 360.0f - mCameraDegree, 0.0f, 0.0f, 1.0f);

        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(programid, "u_mvMatrix"), 1, false, getFloatBuffer(mvMatrix));
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(programid, "u_projMatrix"), 1, false, getFloatBuffer(projMatrix));

        if ((mWidth != width) || (mHeight != height) || (mFormat != format)) {

            if (mTextureYid.get(0) != 0) {
                GLES20.glDeleteTextures(1, mTextureYid);
                mTextureYid.position(0);
                mTextureYid.put(0);
                mTextureYid.position(0);
            }

            if (mTextureUid.get(0) != 0) {
                GLES20.glDeleteTextures(1, mTextureUid);
                mTextureUid.position(0);
                mTextureUid.put(0);
                mTextureUid.position(0);
            }

            if (mTextureVid.get(0) != 0) {
                GLES20.glDeleteTextures(1, mTextureVid);
                mTextureVid.position(0);
                mTextureVid.put(0);
                mTextureVid.position(0);
            }

            if (format == ASVL_PAF_GRAY) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == ASVL_PAF_GRAY16) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, width, height, 0, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if ((format == ASVL_PAF_NV12) || (format == ASVL_PAF_NV21)) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glGenTextures(1, mTextureUid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, width / 2, height / 2, 0, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if ((format == ASVL_PAF_YUYV) || (format == ASVL_PAF_UYVY)) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glGenTextures(1, mTextureUid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width / 2, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE_ALPHA, width, height, 0, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == ASVL_PAF_I444) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
                GLES20.glGenTextures(1, mTextureVid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glGenTextures(1, mTextureUid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == ASVL_PAF_I422H) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
                GLES20.glGenTextures(1, mTextureVid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width / 2, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glGenTextures(1, mTextureUid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width / 2, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == ASVL_PAF_I420) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
                GLES20.glGenTextures(1, mTextureVid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width / 2, height / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glGenTextures(1, mTextureUid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width / 2, height / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == ASVL_PAF_RGB) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if ((format == ASVL_PAF_RGBA) || (format == ASVL_PAF_RGBX)) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glGenTextures(1, mTextureYid);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

            } else if (format == 0) {

            } else {
                Assert.assertTrue("prepare:format not support", false);
            }

            mWidth = width;
            mHeight = height;
            mFormat = format;
        }

        rawBuffer.position(0);
        if (format == ASVL_PAF_GRAY) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height);
        } else if (format == ASVL_PAF_GRAY16) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
        } else if ((format == ASVL_PAF_NV12) || (format == ASVL_PAF_NV21)) {
            rawBuffer.position(width * height);
            rawBuffer.limit(width * height * 3 / 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height / 2, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 3 / 2);
        } else if ((format == ASVL_PAF_YUYV) || (format == ASVL_PAF_UYVY)) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
        } else if (format == ASVL_PAF_UYVY) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
        } else if (format == ASVL_PAF_I444) {
            rawBuffer.position(width * height);
            rawBuffer.limit(width * height * 3);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(width * height);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 3);
        } else if (format == ASVL_PAF_I422H) {
            rawBuffer.position(width * height * 3 / 2);
            rawBuffer.limit(width * height * 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(width * height);
            rawBuffer.limit(width * height * 3 / 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 2);
        } else if (format == ASVL_PAF_I420) {
            rawBuffer.position(width * height * 5 / 4);
            rawBuffer.limit(width * height * 3 / 2);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureVid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(width * height);
            rawBuffer.limit(width * height * 5 / 4);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureUid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width / 2, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 3 / 2);
        } else if (format == ASVL_PAF_RGB) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height * 3);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 3);
        } else if ((format == ASVL_PAF_RGBA) || ((format == ASVL_PAF_RGBX))) {
            rawBuffer.position(0);
            rawBuffer.limit(width * height * 4);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureYid.get(0));
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rawBuffer);

            rawBuffer.position(0);
            rawBuffer.limit(width * height * 4);
        } else {
            Assert.assertTrue("drawTexture:format not support", false);
        }

        float vertices[] = {
                -1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
        };

        float texCoords_base[] = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        float texCoords[] = new float[8];

        float rotateMatrix[] = new float[16];
        float inpos[] = new float[4];
        float outpos[] = new float[4];
        Matrix.setRotateM(rotateMatrix, 0, (float) mCameraDegree, 0.0f, 0.0f, 1.0f);
        for (int i = 0; i < 4; i++) {
            ;
            inpos[0] = texCoords_base[i * 2] * 2.0f - 1.0f;
            inpos[1] = texCoords_base[i * 2 + 1] * 2.0f - 1.0f;
            inpos[2] = 0.0f;
            inpos[3] = 1.0f;
            matrixMultPos(rotateMatrix, inpos, outpos);
            texCoords[i * 2] = (outpos[0] + 1.0f) / 2.0f;
            ;
            texCoords[i * 2 + 1] = (outpos[1] + 1.0f) / 2.0f;
            ;
        }

        int vertex_loc = GLES20.glGetAttribLocation(programid, "aPos");
        GLES20.glVertexAttribPointer(vertex_loc, 3, GLES20.GL_FLOAT, false, 0, getFloatBuffer(vertices));
        GLES20.glEnableVertexAttribArray(vertex_loc);
        int texture_loc = GLES20.glGetAttribLocation(programid, "aText");
        GLES20.glVertexAttribPointer(texture_loc, 2, GLES20.GL_FLOAT, false, 0, getFloatBuffer(texCoords));
        GLES20.glEnableVertexAttribArray(texture_loc);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(vertex_loc);
        GLES20.glDisableVertexAttribArray(texture_loc);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }


    public void drawPoints(int pointsNum, float pointsXY[], int width, int height, float red, float green, float blue, int mode) {
        if (pointsNum > 0) {
            float projMatrix[] = new float[16];
            if (bFrontCamera) {
                Matrix.frustumM(projMatrix, 0, 0.5f, -0.5f, -0.5f, 0.5f, 2.0f, 6.0f);
            } else {
                Matrix.frustumM(projMatrix, 0, -0.5f, 0.5f, -0.5f, 0.5f, 2.0f, 6.0f);
            }

            float mvMatrix[] = new float[16];
            Matrix.setLookAtM(mvMatrix, 0, 0.0f, 0.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mvMatrix, 0, 360.0f - mCameraDegree, 0.0f, 0.0f, 1.0f);

            float[] pointsXYZ = new float[3 * pointsNum];
            for (int i = 0; i < pointsNum; i++) {
                pointsXYZ[i * 3 + 0] = ((2.0f * pointsXY[i * 2]) / (width - 1)) - 1.0f;
                pointsXYZ[i * 3 + 1] = 1.0f - ((2.0f * pointsXY[i * 2 + 1]) / (height - 1));
                pointsXYZ[i * 3 + 2] = 0.0f;
            }

            int programid = createProgram(GLShaderTriangle, VertexShaderTriangle, FragmentShaderTriangle);

            float colors[] = new float[4];
            colors[0] = red;
            colors[1] = green;
            colors[2] = blue;
            colors[3] = 1.0f;

            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(programid, "u_mvMatrix"), 1, false, getFloatBuffer(mvMatrix));
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(programid, "u_projMatrix"), 1, false, getFloatBuffer(projMatrix));
            GLES20.glUniform4fv(GLES20.glGetUniformLocation(programid, "v_color"), 1, getFloatBuffer(colors));

            int vertex_loc = GLES20.glGetAttribLocation(programid, "aPos");
            GLES20.glVertexAttribPointer(vertex_loc, 3, GLES20.GL_FLOAT, false, 0, getFloatBuffer(pointsXYZ));
            GLES20.glEnableVertexAttribArray(vertex_loc);

            GLES20.glDrawArrays(mode, 0, pointsNum);

            GLES20.glDisableVertexAttribArray(vertex_loc);
        }
    }


    private void realeseShader(GLShader shader) {
        if (shader.vertexShader != 0) {
            GLES20.glDeleteShader(shader.vertexShader);
            shader.vertexShader = 0;
        }

        if (shader.fragmentShader != 0) {
            GLES20.glDeleteShader(shader.fragmentShader);
            shader.fragmentShader = 0;
        }

        if (shader.programid != 0) {
            GLES20.glDeleteProgram(shader.programid);
            shader.programid = 0;
        }
    }


    public void uninit() {
        if (mTextureYid.get(0) != 0) {
            GLES20.glDeleteTextures(1, mTextureYid);
            mTextureYid.position(0);
            mTextureYid.put(0);
            mTextureYid.position(0);
        }

        if (mTextureUid.get(0) != 0) {
            GLES20.glDeleteTextures(1, mTextureUid);
            mTextureUid.position(0);
            mTextureUid.put(0);
            mTextureUid.position(0);
        }

        if (mTextureVid.get(0) != 0) {
            GLES20.glDeleteTextures(1, mTextureVid);
            mTextureVid.position(0);
            mTextureVid.put(0);
            mTextureVid.position(0);
        }

        mWidth = 0;
        mHeight = 0;
        mFormat = 0;

        realeseShader(GLShaderTriangle);
        realeseShader(GLShaderTextureGRAY);
        realeseShader(GLShaderTextureGRAY16);
        realeseShader(GLShaderTextureNV12);
        realeseShader(GLShaderTextureNV21);
        realeseShader(GLShaderTextureYUYV);
        realeseShader(GLShaderTextureUYVY);
        realeseShader(GLShaderTextureI440_I422_I444);
        realeseShader(GLShaderTextureRGBX);
        realeseShader(GLShaderTextureRGBA);
    }


    private String VertexShaderTexture =
            "uniform mat4 u_mvMatrix;\n" +
                    "uniform mat4 u_projMatrix;\n" +
                    "attribute vec4 aPos;\n" +
                    "attribute vec2 aText;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = u_projMatrix*u_mvMatrix*aPos;\n" +
                    "  v_texCoord = aText;\n" +
                    "}\n";


    private String FragmentShaderTextureGRAY =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   r = y;\n" +
                    "   g = y;\n" +
                    "   b = y;\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";


    private String FragmentShaderTextureGRAY16 =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float y,y1,y2;\n" +
                    "   y1 = texture2D(u_textureY, v_texCoord).a;\n" +
                    "   y2 = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   y = min(1.0,y1+(y2/256.0));\n" +
                    "   gl_FragColor = vec4(y, y, y, 1.0);\n" +
                    "}\n";

    private String FragmentShaderTextureNV12 =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "uniform sampler2D u_textureU;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y, u, v;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   v = texture2D(u_textureU, v_texCoord).a;\n" +
                    "   u = texture2D(u_textureU, v_texCoord).r;\n" +
                    "   r = y + 1.13983*(v-0.5);\n" +
                    "   g = y - 0.39465*(u-0.5) - 0.58060*(v-0.5);\n" +
                    "   b = y + 2.03211*(u-0.5);\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";

    private String FragmentShaderTextureNV21 =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "uniform sampler2D u_textureU;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y, u, v;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   u = texture2D(u_textureU, v_texCoord).a;\n" +
                    "   v = texture2D(u_textureU, v_texCoord).r;\n" +
                    "   r = y + 1.13983*(v-0.5);\n" +
                    "   g = y - 0.39465*(u-0.5) - 0.58060*(v-0.5);\n" +
                    "   b = y + 2.03211*(u-0.5);\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";

    private String FragmentShaderTextureYUYV =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "uniform sampler2D u_textureU;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y, u, v;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   u = texture2D(u_textureU, v_texCoord).g;\n" +
                    "   v = texture2D(u_textureU, v_texCoord).a;\n" +
                    "   r = y + 1.13983*(v-0.5);\n" +
                    "   g = y - 0.39465*(u-0.5) - 0.58060*(v-0.5);\n" +
                    "   b = y + 2.03211*(u-0.5);\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";

    private String FragmentShaderTextureUYVY =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "uniform sampler2D u_textureU;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y, u, v;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).a;\n" +
                    "   u = texture2D(u_textureU, v_texCoord).r;\n" +
                    "   v = texture2D(u_textureU, v_texCoord).b;\n" +
                    "   r = y + 1.13983*(v-0.5);\n" +
                    "   g = y - 0.39465*(u-0.5) - 0.58060*(v-0.5);\n" +
                    "   b = y + 2.03211*(u-0.5);\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";

    private String FragmentShaderTextureI420_I422_I444 =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY;\n" +
                    "uniform sampler2D u_textureU;\n" +
                    "uniform sampler2D u_textureV;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "   float r, g, b, y, u, v;\n" +
                    "   y = texture2D(u_textureY, v_texCoord).r;\n" +
                    "   u = texture2D(u_textureU, v_texCoord).r;\n" +
                    "   v = texture2D(u_textureV, v_texCoord).r;\n" +
                    "   r = y + 1.13983*(v-0.5);\n" +
                    "   g = y - 0.39465*(u-0.5) - 0.58060*(v-0.5);\n" +
                    "   b = y + 2.03211*(u-0.5);\n" +
                    "   gl_FragColor = vec4(r, g, b, 1.0);\n" +
                    "}\n";


    private String FragmentShaderTextureRGBX =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY; \n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    " gl_FragColor = vec4(texture2D(u_textureY, v_texCoord).xyz,1.0);\n" +
                    "}\n";


    private String FragmentShaderTextureRGBA =
            "precision mediump float;\n" +
                    "uniform sampler2D u_textureY; \n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    " gl_FragColor = texture2D(u_textureY, v_texCoord);\n" +
                    "}\n";


    private String VertexShaderTriangle =
            "uniform mat4 u_mvMatrix;\n" +
                    "uniform mat4 u_projMatrix;\n" +
                    "attribute vec4 aPos;\n" +
                    "void main() {\n" +
                    "  gl_Position = u_projMatrix*u_mvMatrix*aPos;\n" +
                    "}\n";


    private String FragmentShaderTriangle =
            "precision mediump float;\n" +
                    "uniform vec4 v_color;\n" +
                    "void main() {\n" +
                    " gl_FragColor = v_color;\n" +
                    "}\n";


    private int createProgram(GLShader shader, String pVertexSource, String pFragmentSource) {
        if (shader.programid != 0) {
            GLES20.glUseProgram(shader.programid);
            return shader.programid;
        }

        shader.vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, pVertexSource);
        shader.fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, pFragmentSource);
        int programid = GLES20.glCreateProgram();
        if (0 == programid) {
            Assert.assertFalse("glCreateProgram error!", 0 == programid);
            return programid;
        }

        GLES20.glAttachShader(programid, shader.vertexShader);
        GLES20.glAttachShader(programid, shader.fragmentShader);

        GLES20.glLinkProgram(programid);
        int linkStatus[] = new int[1];
        GLES20.glGetProgramiv(programid, GLES20.GL_LINK_STATUS, IntBuffer.wrap(linkStatus));
        if (GLES20.GL_FALSE == linkStatus[0]) {
            Assert.assertFalse("Error linking program:", GLES20.GL_FALSE == linkStatus[0]);
            return programid;
        }

        GLES20.glValidateProgram(programid);
        int validateStatus[] = new int[1];
        GLES20.glGetProgramiv(programid, GLES20.GL_VALIDATE_STATUS, IntBuffer.wrap(validateStatus));
        if (GLES20.GL_FALSE == validateStatus[0]) {
            Assert.assertFalse("Error validate program:", GLES20.GL_FALSE == validateStatus[0]);
            return programid;
        }

        shader.programid = programid;
        GLES20.glUseProgram(shader.programid);
        return programid;
    }


    private int loadShader(int shaderType, String pSource) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, pSource);
        GLES20.glCompileShader(shader);

        String infoLog = null;
        if (GLES20.glIsShader(shader)) {
            infoLog = GLES20.glGetShaderInfoLog(shader);
        } else {
            infoLog = GLES20.glGetProgramInfoLog(shader);
        }

        if ((infoLog != null) && (infoLog.length() > 0)) {
            Log.e(TAG, infoLog);
        }

        int compiled[] = new int[1];
        compiled[0] = GLES20.GL_FALSE;
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, IntBuffer.wrap(compiled));
        if (GLES20.GL_FALSE == compiled[0]) {
            Log.e(TAG, "compile error! " + pSource);
            return shader;
        }
        return shader;
    }

    private FloatBuffer getFloatBuffer(float[] a) {
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
        mbb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = mbb.asFloatBuffer();
        floatBuffer.put(a);
        floatBuffer.position(0);
        return floatBuffer;
    }

    private void matrixMultPos(float[] m, float[] inpos, float[] outpos) {
        for (int i = 0; i < 4; i++) {
            outpos[i] = (m[i + 0] * inpos[0]) + (m[i + 4] * inpos[1]) + (m[i + 8] * inpos[2]) + (m[i + 12] * inpos[3]);
        }
    }
}
