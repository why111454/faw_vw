package com.fax.utils.frameAnim;

public interface FrameAnimListener {
	public void onPlaying(FrameAnimation animation, int frameIndex);
	public void onFinish(FrameAnimation animation);
	public void onStart(FrameAnimation animation);
}
