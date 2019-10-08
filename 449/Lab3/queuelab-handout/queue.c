/*
 * This program implements a queue supporting both FIFO and LIFO
 * operations.
 *
 * It uses a singly-linked list to represent the set of queue elements
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "harness.h"
#include "queue.h"
int size = 0;
/*
  Create empty queue.
  Return NULL if could not allocate space.
*/
queue_t *q_new()
{
    queue_t *q =  malloc(sizeof(queue_t));
    /* What if malloc returned NULL? */
	if (q == NULL)
	{
		return NULL;
	}
    	q->head = NULL;
	q->tail = NULL;
    return q;
}

/* Free all storage used by queue */
void q_free(queue_t *q)
{
    /* How about freeing the list elements and the strings? */
    /* Free queue structure */
	if (q == NULL)
	{
		return; 
	}

	list_ele_t* temp;
	while (q->head != NULL)
	{
		temp = q->head;
		q->head = q->head->next;
	
		free(temp);
	}
	free(q);
	size = 0;    
}


/*
  Attempt to insert element at head of queue.
  Return true if successful.
  Return false if q is NULL or could not allocate space.
  Argument s points to the string to be stored.
  The function must explicitly allocate space and copy the string into it.
 */
bool q_insert_head(queue_t *q, char *s)
{
    list_ele_t *newh;
    /* What should you do if the q is NULL? */
    newh = malloc(sizeof(list_ele_t));
    /* Don't forget to allocate space for the string and copy it */
    /* What if either call to malloc returns NULL? */
	if (q != NULL && newh != NULL && q->tail != NULL)
        {	
                newh->next = q->head;
                q->head = newh;
        }
	else if (q != NULL && q->tail == NULL)
	{
		newh->next = q->head;
		q->head = newh;
		q->tail = newh;
	}	
	else if (newh != NULL)
	{
		free(newh);
		return false;
	}
	else
	{
		return false;
	}
			
	newh->value = strdup(s);
	if (newh->value == NULL)
	{
		return false;
	}

	size++;
    	return true;
}


/*
  Attempt to insert element at tail of queue.
  Return true if successful.
  Return false if q is NULL or could not allocate space.
  Argument s points to the string to be stored.
  The function must explicitly allocate space and copy the string into it.
 */
bool q_insert_tail(queue_t *q, char *s)
{
    /* You need to write the complete code for this function */
    /* Remember: It should operate in O(1) time */
	list_ele_t *newt;
	newt = malloc(sizeof(list_ele_t));
	if (q != NULL && newt != NULL && size != 0)
	{
		newt->next = NULL;
		q->tail->next = newt;
		q->tail = newt;
	}
	else if (q != NULL && size == 0)
	{
		newt->next = NULL;
		q->tail = newt;
		q->head = newt;	
	}
	else if (newt != NULL)
	{
		free(newt);
		return false;
	}
	else 
	{
		return false;
	}
	newt->value = strdup(s);
	if (newt->value == NULL)
	{
		return false;
	}
	size++;
	return true;
}

/*
  Attempt to remove element from head of queue.
  Return true if successful.
  Return false if queue is NULL or empty.
  If sp is non-NULL and an element is removed, copy the removed string to *sp
  (up to a maximum of bufsize-1 characters, plus a null terminator.)
  The space used by the list element and the string should be freed.
*/
bool q_remove_head(queue_t *q, char *sp, size_t bufsize)
{	
	list_ele_t *temp;
	size_t i =0;
	if (q == NULL || q->head == NULL)
	{
		return false;
	}
	if (sp != NULL)
	{
		while(*(q->head->value) && (i < bufsize-1))
		{
			*sp =*(q->head->value);
			sp++;
			q->head->value++;	 
			i++;
		}
		*sp = '\0';
	}
	temp = q->head;
	q->head = q->head->next;
	free(temp);
	size--;
	return true;
	
}

/*
  Return number of elements in queue.
  Return 0 if q is NULL or empty
 */
int q_size(queue_t *q)
{
    /* You need to write the code for this function */
    /* Remember: It should operate in O(1) time */
    return size;
}

/*
  Reverse elements in queue
  No effect if q is NULL or empty
  This function should not allocate or free any list elements
  (e.g., by calling q_insert_head, q_insert_tail, or q_remove_head).
  It should rearrange the existing ones.
 */
void q_reverse(queue_t *q)
{
	list_ele_t *curr;
	list_ele_t *prev = NULL;
	list_ele_t *temp;
	if (q == NULL)
	{
		return ;
	}
	curr = q->head;
	q->tail = q->head;
	while(curr != NULL)
	{
		temp = curr->next;
		curr->next = prev;
		prev = curr;
		curr = temp;
	}
	q->head = prev;
	
}


