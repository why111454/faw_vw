package com.fax.utils.frameAnim;

import java.util.ArrayList;

public class FrameList extends ArrayList<Frame>{
	private boolean isOneShot;

	public void setOneShot(boolean isOneShot) {
		this.isOneShot = isOneShot;
	}

	public boolean isOneShot() {
		return isOneShot;
	}
}
