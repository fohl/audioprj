/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *                                                                                   *
 *  WONDER - Wave field synthesis Of New Dimensions of Electronic music in Realtime  *
 *  http://swonder.sourceforge.net                                                   *
 *                                                                                   *
 *                                                                                   *
 *  Technische Universität Berlin, Germany                                           *
 *  Audio Communication Group                                                        *
 *  www.ak.tu-berlin.de                                                              *
 *  Copyright 2006-2008                                                              *
 *                                                                                   *
 *                                                                                   *
 *  This program is free software; you can redistribute it and/or modify             *
 *  it under the terms of the GNU General Public License as published by             *
 *  the Free Software Foundation; either version 2 of the License, or                *
 *  (at your option) any later version.                                              *
 *                                                                                   *
 *  This program is distributed in the hope that it will be useful,                  *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                    *
 *  GNU General Public License for more details.                                     *
 *                                                                                   *
 *  You should have received a copy of the GNU General Public License                *
 *  along with this program; if not, write to the Free Software                      *
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.       *
 *                                                                                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

#include <sstream>
#include "sourcearray.h"
#include "fwonder_config.h"


SourceArray::SourceArray( int noSources, int IRPartitionSize, int maxIRLength, int tailPartitionSize, int tailLength )
{
    for( int i = 1; i <= noSources; ++i )
    {
        std::stringstream inputName;
	inputName << "input" << i;

        std::stringstream IRPath;
	IRPath << fwonderConf->BRIRPath << "/source" << i << "/";

        std::stringstream tailPath;
	tailPath << fwonderConf->BRIRPath << "/source" << i << "/";

	push_back( new SourceAggregate( inputName.str(), IRPartitionSize, maxIRLength, maxIRLength / IRPartitionSize,
                                        tailLength / tailPartitionSize, tailPartitionSize, IRPath.str(), tailPath.str() ) );

     }
}

SourceArray::~SourceArray()
{
    while( ! empty() )
    {
        delete back();
        pop_back();
    }
}

SourceArray::SourceArray( const SourceArray& other )
{
    // just preventing copying
}
