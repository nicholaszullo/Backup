#define SEM_H
#include <linux/sched.h>
struct cs1550_sem
{
	int value;
	int pqnum;
	struct Node* head;
};
struct Node
{
	struct Node* next;
	struct task_struct* data;
	int priority;
};

