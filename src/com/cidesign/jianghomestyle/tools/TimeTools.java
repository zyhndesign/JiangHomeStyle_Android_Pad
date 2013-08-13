package com.cidesign.jianghomestyle.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTools
{
	public static String getTime(int milliseconds)
	{
		if (milliseconds == 0)
		{
			return "00:00:00";
		}
		else
		{
			StringBuilder sBuilder = new StringBuilder();
			int seconds_time = milliseconds / 1000;
			int hours = 0;
			if ((seconds_time / 60) > 60)
			{
				hours = seconds_time / 60 / 60;
				if (hours > 9)
				{
					sBuilder.append(hours + ":");
				}
				else
				{
					sBuilder.append("0" + hours + ":");
				}
				int minutes = seconds_time / 60 % 60;
				if (minutes > 59)
				{
					minutes = minutes / 60;
					if (minutes > 9)
					{
						sBuilder.append(minutes + ":");
					}
					else
					{
						sBuilder.append("0" + minutes + ":");
					}
					int seconds = seconds_time % 60;
					if (seconds > 9)
					{
						sBuilder.append(seconds);
					}
					else
					{
						sBuilder.append("0" + seconds);
					}
				}
				else
				{
					if (minutes > 9)
					{
						sBuilder.append(minutes + ":");
					}
					else
					{
						sBuilder.append("0" + minutes + ":");
					}
					sBuilder.append("00");
				}
			}
			else
			{
				sBuilder.append("00:");
				int minutes = seconds_time / 60;

				if (minutes > 9)
				{
					sBuilder.append(minutes + ":");
				}
				else
				{
					sBuilder.append("0" + minutes + ":");
				}
				int seconds = seconds_time % 60;
				if (seconds > 9)
				{
					sBuilder.append(seconds);
				}
				else
				{
					sBuilder.append("0" + seconds + "");
				}

			}
			return sBuilder.toString();
		}
	}

	public static String getCurrentDateTime()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	public static String getTimeByTimestap(long timestap)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(timestap));
	}
}
