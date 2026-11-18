// hello: print "Hello, World" in all 16 CGA colors using ANSI escapes.
#include "types.h"
#include "user.h"

int
main(void)
{
  int i;

  for(i = 30; i <= 37; i++)       // normal colors (30 = black, on gray bg)
    printf(1, "\033[%d%sm%d: Hello, World\033[0m\n", i, i == 30 ? ";47" : "", i);
  for(i = 90; i <= 97; i++)       // bright colors
    printf(1, "\033[%dm%d: Hello, World\033[0m\n", i, i);
  exit();
}
