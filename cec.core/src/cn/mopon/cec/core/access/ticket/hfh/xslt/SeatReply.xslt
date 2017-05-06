<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="data">
		<data>
			<result>0</result>
			<xsl:variable name="hallCode" select="seatPlan/@hallId" />
			<Hall>
				<code><xsl:value-of select="$hallCode"/></code>
			    <seats>
			    	<xsl:for-each select="seatPlan/effective[1]/section[1]">
			    		<xsl:variable name="GroupCode" select="@id" />
			    		<xsl:for-each select="seat">
			    			<Seat>
			    				<code><xsl:value-of select="@rowId" />_<xsl:value-of select="@columnId" /></code>
			    				<groupCode><xsl:value-of select="$GroupCode" /></groupCode>
			    				<rowNum><xsl:value-of select="@rowId" /></rowNum> 
			    				<colNum><xsl:value-of select="@columnId" /></colNum>
			    				<xCoord><xsl:value-of select="@rowNum" /></xCoord>
			    				<yCoord><xsl:value-of select="@columnNum" /></yCoord>
			    				<loveCode><xsl:value-of select="@typeInd" /></loveCode>
			    				<status>
				    				<xsl:choose>
				    					<xsl:when test="@damagedFlg = 'N'">1</xsl:when>
				    					<xsl:otherwise>0</xsl:otherwise>
				    				</xsl:choose>
				    			</status>
				    			<type>
				    				<xsl:choose>
				    					<xsl:when test="@loveInd = '0'">1</xsl:when>
										<xsl:otherwise>2</xsl:otherwise>
									</xsl:choose>
								</type>
						         <hall>
						         	<code><xsl:value-of select="$hallCode" /></code>
						         </hall>
						      </Seat>
						   </xsl:for-each>
					</xsl:for-each>
			   </seats>
			</Hall>
		</data>
	</xsl:template>
</xsl:stylesheet>