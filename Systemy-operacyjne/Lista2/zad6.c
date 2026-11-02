#include <stdio.h>

#define ESC "\033["
#define RESET ESC "0m"

/* truecolor ma 16 777 216 kolorów, więc przechodzimy je z krokiem */
#define TRUECOLOR_STEP 51

int main(void) {
  printf("-- 8 kolorów podstawowych: ESC[30m .. ESC[37m --\n");
  for (int code = 30; code <= 37; code++)
    printf(ESC "%dm%3d Hello, World!" RESET "\n", code, code);

  printf("-- 8 kolorów jasnych: ESC[90m .. ESC[97m --\n");
  for (int code = 90; code <= 97; code++)
    printf(ESC "%dm%3d Hello, World!" RESET "\n", code, code);

  printf("-- paleta 256 kolorów: ESC[38;5;Nm --\n");
  for (int n = 0; n < 256; n++)
    printf(ESC "38;5;%dm%3d Hello, World!" RESET "\n", n, n);

  printf("-- próbka truecolor: ESC[38;2;R;G;Bm --\n");
  for (int r = 0; r < 256; r += TRUECOLOR_STEP)
    for (int g = 0; g < 256; g += TRUECOLOR_STEP)
      for (int b = 0; b < 256; b += TRUECOLOR_STEP)
        printf(ESC "38;2;%d;%d;%dm#%02x%02x%02x Hello, World!" RESET "\n", r, g,
               b, r, g, b);

  return 0;
}
