package org.homebrew;

import java.util.Random;

class Perlin3D
{
    private static final int MAX_VEC_COORD_VALUE = 127;
    private static final double VEC_MULT = 0.1;
    public static void perlin3D(Random r, byte[] world, byte[] world0, int[] aux, GeneratingGUI report)
    {
	System.out.println("work");
	int mx = -99999, mn = 99999;
	int[][][][] vertexRandom = new int[2][2][2][4];
	for (int x = 0; x < 2; x++)
		for (int y = 0; y < 2; y++)
			for (int z = 0; z < 2; z++)
			{
				vertexRandom[x][y][z][0] = r.nextInt(MAX_VEC_COORD_VALUE * 2) - MAX_VEC_COORD_VALUE;
				vertexRandom[x][y][z][1] = r.nextInt(127 * 2) - 127;
				vertexRandom[x][y][z][2] = r.nextInt(127 * 2) - 127;
				vertexRandom[x][y][z][3] = 1;//r.nextInt(128); // множитель производной
			}
	for(int i = 0; i < 128*128*128; i++)
        {
		int blockX = (i >> 14);            // x-координата
		int blockZ = (i >> 7) % 128; // y-координата
		int blockY = i % 128;              // z-координата

		int[][][] value = new int[2][2][2];
		for (int x = 0; x < 2; x++)
			for (int y = 0; y < 2; y++)
				for (int z = 0; z < 2; z++)
				{
					int[][][][] vec = new int[2][2][2][3];
					vec[x][y][z][0] = blockX - x * 127; //
					vec[x][y][z][1] = blockZ - y * 127; // вектор от вершины мира (x, y, z) до блока i
					vec[x][y][z][2] = blockY - z * 127; //
					value[x][y][z] = (vec[x][y][z][0]*vertexRandom[x][y][z][0] + vec[x][y][z][1]*vertexRandom[x][y][z][1] + vec[x][y][z][2]*vertexRandom[x][y][z][2]) * vertexRandom[x][y][z][3];
				}
		int[] halfvalue = new int[6];
		halfvalue[0] = value[0][0][0] + blockX * (value[0][0][0] - value[1][0][0]);
		halfvalue[1] = value[0][1][0] + blockX * (value[0][1][0] - value[1][1][0]);
		halfvalue[2] = value[0][0][1] + blockX * (value[0][0][1] - value[1][0][1]);
		halfvalue[3] = value[0][1][1] + blockX * (value[0][1][1] - value[1][1][1]);

		halfvalue[4] = halfvalue[0] + blockZ * (halfvalue[0] - halfvalue[1]);
		halfvalue[5] = halfvalue[2] + blockZ * (halfvalue[2] - halfvalue[3]);

		int perlinValue = halfvalue[4] + blockY * (halfvalue[4] - halfvalue[5]);

		if (perlinValue > mx)
			mx = perlinValue;
		if (perlinValue < mn)
			mn = perlinValue;

		if (perlinValue > 0)
			world[i] = 1;
		else
			world[i] = 0;
		double percent = i + 1;
		percent = percent / (128*128*128);
            	if(report != null)
                	report.reportProgress(percent);
        }
	System.out.println("max = "+mx);
        System.out.println("min = "+mn);
    }
}
