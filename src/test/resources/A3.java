class A {

	public static void main(String args[]) {
		int r = a(1, 2) + b(1, 2);
		System.out.println(r);
	}

	public static char c(char i, char j) {
		return i + j;
	}

	public static int a(int i, int j) {
		return i + j;
	}

	public static int b(int i, int j) {
		return i * j;
	}

}