// expr: simple integer arithmetic, e.g. "expr 2 + 2".
#include "types.h"
#include "user.h"

// atoi() from ulib.c does not handle a minus sign, so wrap it.
int
toint(char *s)
{
  if(s[0] == '-')
    return -atoi(s + 1);
  return atoi(s);
}

int
main(int argc, char *argv[])
{
  int a, b;
  char op;

  if(argc != 4 || argv[2][1] != 0){
    printf(2, "usage: expr num op num   (op: + - * / %%)\n");
    exit();
  }

  a = toint(argv[1]);
  op = argv[2][0];
  b = toint(argv[3]);

  if((op == '/' || op == '%') && b == 0){
    printf(2, "expr: division by zero\n");
    exit();
  }

  switch(op){
  case '+': printf(1, "%d\n", a + b); break;
  case '-': printf(1, "%d\n", a - b); break;
  case '*': printf(1, "%d\n", a * b); break;
  case '/': printf(1, "%d\n", a / b); break;
  case '%': printf(1, "%d\n", a % b); break;
  default:
    printf(2, "expr: unknown operator %c\n", op);
  }
  exit();
}
