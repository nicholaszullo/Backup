#define SEM_H
#include <linux/sched.h>
struct cs1550_sem
{
   int value;
   int pqnum;
   struct task_struct *pq[100];
};

