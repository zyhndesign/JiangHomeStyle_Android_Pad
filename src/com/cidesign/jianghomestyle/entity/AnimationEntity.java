package com.cidesign.jianghomestyle.entity;

import android.view.View;

public class AnimationEntity
{
	private View view; //���ƶ�����ͼ����
	private int move_distance; //�ƶ�����
	private int move_direction;//�ƶ�����(0:����  1:���� 2:���� 3:����)
	private int screen_width; //��Ļ���
	private int screen_height;//��Ļ�߶�
	private int view_width; //��ͼ���
	private int view_height; //��ͼ�߶�

	public View getView()
	{
		return view;
	}
	public void setView(View view)
	{
		this.view = view;
	}
	public int getMove_distance()
	{
		return move_distance;
	}
	public void setMove_distance(int move_distance)
	{
		this.move_distance = move_distance;
	}
	public int getMove_direction()
	{
		return move_direction;
	}
	public void setMove_direction(int move_direction)
	{
		this.move_direction = move_direction;
	}
	public int getScreen_width()
	{
		return screen_width;
	}
	public void setScreen_width(int screen_width)
	{
		this.screen_width = screen_width;
	}
	public int getScreen_height()
	{
		return screen_height;
	}
	public void setScreen_height(int screen_height)
	{
		this.screen_height = screen_height;
	}
	public int getView_width()
	{
		return view_width;
	}
	public void setView_width(int view_width)
	{
		this.view_width = view_width;
	}
	public int getView_height()
	{
		return view_height;
	}
	public void setView_height(int view_height)
	{
		this.view_height = view_height;
	}
	
}
